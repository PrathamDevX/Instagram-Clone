package com.company.InstagramClone.data

import com.company.InstagramClone.data.model.CommentRecord
import com.company.InstagramClone.data.model.PostRecord
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.data.model.StoryRecord
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class SocialRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun getCollectionName(isPost: Boolean) = if (isPost) "posts" else "reels"

    suspend fun savePost(post: PostRecord): Result<Unit> = try {
        val ref = firestore.collection("posts").document()
        firestore.collection("posts").document(ref.id).set(post.copy(postId = ref.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun saveStory(story: StoryRecord): Result<Unit> = try {
        val ref = firestore.collection("stories").document()
        firestore.collection("stories").document(ref.id).set(story.copy(storyId = ref.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun saveReel(reel: ReelRecord): Result<Unit> = try {
        val ref = firestore.collection("reels").document()
        firestore.collection("reels").document(ref.id).set(reel.copy(reelId = ref.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun toggleLike(postId: String, isPost: Boolean, userId: String): Result<Boolean> = try {
        val collection = getCollectionName(isPost)
        val postRef = firestore.collection(collection).document(postId)
        val likeRef = postRef.collection("likes").document(userId)

        val doc = likeRef.get().await()
        val isLiked = if (doc.exists()) {
            // Unlike
            likeRef.delete().await()
            postRef.update("likesCount", FieldValue.increment(-1)).await()
            false
        } else {
            // Like
            likeRef.set(mapOf("timestamp" to Timestamp.now())).await()
            postRef.update("likesCount", FieldValue.increment(1)).await()
            true
        }
        Result.success(isLiked)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun checkIfLiked(postId: String, isPost: Boolean, userId: String): Result<Boolean> = try {
        val collection = getCollectionName(isPost)
        val doc = firestore.collection(collection).document(postId)
            .collection("likes").document(userId).get().await()
        Result.success(doc.exists())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun addComment(postId: String, isPost: Boolean, comment: CommentRecord): Result<Unit> = try {
        val collection = getCollectionName(isPost)
        val postRef = firestore.collection(collection).document(postId)
        val commentRef = postRef.collection("comments").document()
        
        commentRef.set(comment.copy(commentId = commentRef.id)).await()
        postRef.update("commentsCount", FieldValue.increment(1)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getComments(postId: String, isPost: Boolean): Result<List<CommentRecord>> = try {
        val collection = getCollectionName(isPost)
        val snapshot = firestore.collection(collection).document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        
        Result.success(snapshot.toObjects(CommentRecord::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getActiveStories(): Result<List<StoryRecord>> = try {
        val now = Timestamp.now()
        val snapshot = firestore.collection("stories")
            .whereGreaterThan("expiresAt", now)
            .get().await()
        
        val stories = snapshot.toObjects(StoryRecord::class.java)
        Result.success(stories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getAllReels(): Result<List<ReelRecord>> = try {
        val snapshot = firestore.collection("reels")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val reels = snapshot.toObjects(ReelRecord::class.java)
        
        val finalReels = if (currentUid != null) {
            reels.map { reel ->
                val isLiked = firestore.collection("reels").document(reel.reelId)
                    .collection("likes").document(currentUid).get().await().exists()
                reel.copy(isLiked = isLiked)
            }
        } else {
            reels
        }
        
        Result.success(finalReels)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserReels(userId: String): Result<List<ReelRecord>> = try {
        val snapshot = firestore.collection("reels")
            .whereEqualTo("userId", userId)
            .get().await()
        
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val reels = snapshot.toObjects(ReelRecord::class.java)
        
        val finalReels = if (currentUid != null) {
            reels.map { reel ->
                val isLiked = firestore.collection("reels").document(reel.reelId)
                    .collection("likes").document(currentUid).get().await().exists()
                reel.copy(isLiked = isLiked)
            }
        } else {
            reels
        }
        
        Result.success(finalReels.sortedByDescending { it.timestamp })
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun followUser(currentUserId: String, targetUserId: String): Result<Unit> = try {
        val currentUserRef = firestore.collection("users").document(currentUserId)
        val targetUserRef = firestore.collection("users").document(targetUserId)
        
        firestore.runBatch { batch ->
            // Update counts
            batch.update(currentUserRef, "followingCount", FieldValue.increment(1))
            batch.update(targetUserRef, "followersCount", FieldValue.increment(1))
            
            // Add relationship records
            batch.set(currentUserRef.collection("following").document(targetUserId), mapOf("timestamp" to Timestamp.now()))
            batch.set(targetUserRef.collection("followers").document(currentUserId), mapOf("timestamp" to Timestamp.now()))
        }.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun unfollowUser(currentUserId: String, targetUserId: String): Result<Unit> = try {
        val currentUserRef = firestore.collection("users").document(currentUserId)
        val targetUserRef = firestore.collection("users").document(targetUserId)
        
        firestore.runBatch { batch ->
            // Update counts
            batch.update(currentUserRef, "followingCount", FieldValue.increment(-1))
            batch.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            
            // Remove relationship records
            batch.delete(currentUserRef.collection("following").document(targetUserId))
            batch.delete(targetUserRef.collection("followers").document(currentUserId))
        }.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun checkIfFollowing(currentUserId: String, targetUserId: String): Result<Boolean> = try {
        val doc = firestore.collection("users").document(currentUserId)
            .collection("following").document(targetUserId).get().await()
        Result.success(doc.exists())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
