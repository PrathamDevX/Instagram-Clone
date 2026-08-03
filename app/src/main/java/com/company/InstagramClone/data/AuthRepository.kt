package com.company.InstagramClone.data

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.company.InstagramClone.utils.CloudinaryHelper
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

interface AuthRepository {
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithCredential(credential: AuthCredential): Result<Unit>
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
    suspend fun getUserProfile(): Result<UserProfile?>
    suspend fun getPosts(): Result<List<com.company.InstagramClone.feature.home.Post>>
    fun isUserLoggedIn(): Boolean
    fun signOut()
    fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )
}

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        FirebaseFirestore.setLoggingEnabled(true)
    }
) : AuthRepository {
    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Result<Unit> {
        return try {
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            android.util.Log.d("AUTH_REPO", "Saving profile for user: ${currentUser?.uid}")
            if (currentUser == null) {
                return Result.failure(Exception("User not logged in"))
            }
            val uid = currentUser.uid
            android.util.Log.d("AUTH_REPO", "Firestore path: users/$uid")
            firestore.collection("users").document(uid).set(profile).await()
            android.util.Log.d("AUTH_REPO", "Firestore save success")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("AUTH_REPO", "Firestore save failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile?> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.success(null)
            android.util.Log.d("AUTH_REPO", "Fetching profile for UID: $uid")
            val document = firestore.collection("users").document(uid).get().await()
            val profile = document.toObject(UserProfile::class.java)
            android.util.Log.d("AUTH_REPO", "Profile fetch success: $profile")
            Result.success(profile)
        } catch (e: Exception) {
            android.util.Log.e("AUTH_REPO", "Profile fetch failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getPosts(): Result<List<com.company.InstagramClone.feature.home.Post>> {
        return try {
            android.util.Log.d("AUTH_REPO", "Fetching all posts from Firestore...")
            val snapshot = firestore.collection("posts").get().await()
            val currentUid = auth.currentUser?.uid
            
            val posts = snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data
                    android.util.Log.d("AUTH_REPO", "Post Doc [${doc.id}]: $data")
                    
                    val mediaUrls = doc.get("mediaUrls") as? List<*>
                    val firstUrl = mediaUrls?.firstOrNull()?.toString() ?: ""
                    val mediaType = CloudinaryHelper.getMediaType(
                        url = firstUrl,
                        currentType = doc.getString("mediaType") ?: "image"
                    )
                    
                    com.company.InstagramClone.feature.home.Post(
                        id = doc.id.hashCode(),
                        postId = doc.id,
                        userId = doc.getString("userId") ?: "",
                        username = doc.getString("username") ?: "Anonymous",
                        userImageUrl = doc.getString("profileImageUrl") ?: "",
                        postImageUrl = firstUrl,
                        mediaType = mediaType,
                        caption = doc.getString("caption") ?: "",
                        likesCount = doc.getLong("likesCount")?.toInt() ?: 0,
                        isLiked = false, // We'll update this in a separate pass or just fetch likes separately
                        timeAgo = "Just now"
                    )
                } catch (e: Exception) {
                    android.util.Log.e("AUTH_REPO", "Error mapping post doc [${doc.id}]: ${e.message}")
                    null
                }
            }
            
            // Second pass to check likes if logged in
            val finalPosts = if (currentUid != null) {
                posts.map { post ->
                    val isLiked = firestore.collection("posts").document(post.postId)
                        .collection("likes").document(currentUid).get().await().exists()
                    post.copy(isLiked = isLiked)
                }
            } else {
                posts
            }
            
            android.util.Log.d("AUTH_REPO", "Posts fetch success. Count: ${finalPosts.size}")
            Result.success(finalPosts)
        } catch (e: Exception) {
            android.util.Log.e("AUTH_REPO", "Posts fetch failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun verifyPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
