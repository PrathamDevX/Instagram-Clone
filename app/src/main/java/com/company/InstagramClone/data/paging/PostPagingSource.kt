package com.company.InstagramClone.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.feature.home.Post
import com.company.InstagramClone.utils.CloudinaryHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class PostPagingSource(
    private val socialRepository: SocialRepository,
    private val currentUserId: String?
) : PagingSource<DocumentSnapshot, Post>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Post>): DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Post> {
        return try {
            val snapshot = socialRepository.getPostsPage(params.loadSize, params.key)
            val documents = snapshot.documents
            
            // Fetch all liked IDs for this user in one query (Bulk check)
            val likedIds = if (currentUserId != null) {
                socialRepository.getLikedIds(currentUserId, true).getOrDefault(emptySet())
            } else {
                emptySet()
            }

            val posts = documents.map { doc ->
                val mediaUrls = doc.get("mediaUrls") as? List<*>
                val firstUrl = mediaUrls?.firstOrNull()?.toString() ?: ""
                val mediaType = CloudinaryHelper.getMediaType(firstUrl, doc.getString("mediaType") ?: "image")
                
                Post(
                    id = doc.id.hashCode(),
                    postId = doc.id,
                    userId = doc.getString("userId") ?: "",
                    username = doc.getString("username") ?: "Anonymous",
                    userImageUrl = doc.getString("profileImageUrl") ?: "",
                    postImageUrl = firstUrl,
                    mediaType = mediaType,
                    caption = doc.getString("caption") ?: "",
                    likesCount = doc.getLong("likesCount")?.toInt() ?: 0,
                    isLiked = likedIds.contains(doc.id),
                    timeAgo = "Just now"
                )
            }

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if (documents.size < params.loadSize) null else documents.lastOrNull()
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
