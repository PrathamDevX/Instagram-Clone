package com.company.InstagramClone.feature.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.MediaRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.feature.home.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
        val userProfile: UserProfile?,
        val userPosts: List<Post> = emptyList(),
        val userReels: List<ReelRecord> = emptyList(),
        val isFollowing: Boolean = false,
        val isCurrentUser: Boolean = true
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository(),
    private val mediaRepository: MediaRepository = MediaRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        fetchProfileData()
    }

    fun fetchProfileData(targetUserId: String? = null) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            android.util.Log.d("PROFILE_VM", "Fetching profile data for: $targetUserId")
            
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid
            val filterUid = targetUserId ?: currentUid
            
            if (filterUid == null) {
                _profileState.value = ProfileState.Error("User not identified")
                return@launch
            }

            try {
                // Fetch data in parallel or sequence
                val doc = FirebaseFirestore.getInstance()
                    .collection("users").document(filterUid).get().await()
                val profile = doc.toObject(UserProfile::class.java)

                val postsResult = repository.getPosts()
                val reelsResult = socialRepository.getUserReels(filterUid)
                
                // Process results off-thread
                val (userPosts, isFollowing) = withContext(Dispatchers.Default) {
                    val allPosts = postsResult.getOrDefault(emptyList())
                    val filteredPosts = allPosts.filter { it.userId == filterUid }
                    
                    val followingStatus = if (currentUid != null && targetUserId != null && targetUserId != currentUid) {
                        socialRepository.checkIfFollowing(currentUid, targetUserId).getOrDefault(false)
                    } else {
                        false
                    }
                    
                    Pair(filteredPosts, followingStatus)
                }

                _profileState.value = ProfileState.Success(
                    userProfile = profile,
                    userPosts = userPosts,
                    userReels = reelsResult.getOrDefault(emptyList()),
                    isFollowing = isFollowing,
                    isCurrentUser = targetUserId == null || targetUserId == currentUid
                )
            } catch (e: Exception) {
                android.util.Log.e("PROFILE_VM", "Fetch failed: ${e.message}")
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun toggleFollow() {
        val currentState = _profileState.value
        if (currentState is ProfileState.Success) {
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val targetUid = currentState.userProfile?.uid ?: return
            
            viewModelScope.launch {
                if (currentState.isFollowing) {
                    socialRepository.unfollowUser(currentUid, targetUid)
                } else {
                    socialRepository.followUser(currentUid, targetUid)
                }
                fetchProfileData(targetUid)
            }
        }
    }

    fun uploadProfilePicture(context: android.content.Context, uri: Uri) {
        viewModelScope.launch {
            val currentState = _profileState.value
            if (currentState is ProfileState.Success) {
                _profileState.value = ProfileState.Loading
                val uploadResult = mediaRepository.uploadImage(context, uri)
                uploadResult.onSuccess { imageUrl ->
                    val updatedProfile = currentState.userProfile?.copy(profileImageUrl = imageUrl)
                    if (updatedProfile != null) {
                        repository.saveUserProfile(updatedProfile)
                            .onSuccess {
                                fetchProfileData()
                            }
                            .onFailure {
                                _profileState.value = ProfileState.Error(it.message ?: "Failed to update Firestore")
                            }
                    }
                }.onFailure {
                    _profileState.value = ProfileState.Error(it.message ?: "Failed to upload image")
                }
            }
        }
    }

    fun logout() {
        repository.signOut()
    }
}
