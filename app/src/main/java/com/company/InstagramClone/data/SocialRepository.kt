package com.company.InstagramClone.data

import com.company.InstagramClone.data.model.PostRecord
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.data.model.StoryRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SocialRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
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
}
