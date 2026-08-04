package com.company.InstagramClone.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.model.ReelRecord
import com.google.firebase.firestore.DocumentSnapshot

class ReelPagingSource(
    private val socialRepository: SocialRepository,
    private val currentUserId: String?
) : PagingSource<DocumentSnapshot, ReelRecord>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, ReelRecord>): DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, ReelRecord> {
        return try {
            val snapshot = socialRepository.getReelsPage(params.loadSize, params.key)
            val documents = snapshot.documents
            
            val likedIds = if (currentUserId != null) {
                socialRepository.getLikedIds(currentUserId, false).getOrDefault(emptySet())
            } else {
                emptySet()
            }

            val reels = documents.map { doc ->
                doc.toObject(ReelRecord::class.java)!!.copy(
                    reelId = doc.id,
                    isLiked = likedIds.contains(doc.id)
                )
            }

            LoadResult.Page(
                data = reels,
                prevKey = null,
                nextKey = if (documents.size < params.loadSize) null else documents.lastOrNull()
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
