package com.company.InstagramClone.feature.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.MediaRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.feature.home.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
        val userProfile: UserProfile?,
        val userPosts: List<Post> = emptyList(),
        val followersCount: Int = 88,
        val followingCount: Int = 100
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository(),
    private val mediaRepository: MediaRepository = MediaRepository()
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        fetchProfileData()
    }

    fun fetchProfileData() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            android.util.Log.d("PROFILE_VM", "Fetching profile data...")
            
            val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            val profileResult = repository.getUserProfile()
            val postsResult = repository.getPosts()

            if (profileResult.isSuccess) {
                val profile = profileResult.getOrNull()
                val allPosts = postsResult.getOrDefault(emptyList())
                
                // Prioritize currentUid for filtering
                val filterUid = profile?.uid ?: currentUid
                val userPosts = if (filterUid != null) {
                    allPosts.filter { it.userId == filterUid }
                } else {
                    emptyList()
                }

                android.util.Log.d("PROFILE_VM", "Profile data fetch success. User posts found: ${userPosts.size}")
                _profileState.value = ProfileState.Success(
                    userProfile = profile,
                    userPosts = userPosts
                )
            } else {
                val errorMsg = profileResult.exceptionOrNull()?.message ?: "Failed to load profile"
                val errorMsgFull = if (errorMsg.contains("offline", ignoreCase = true)) {
                    "The app is offline. Please check your internet connection and ensure Firestore is enabled in the Firebase Console."
                } else {
                    errorMsg
                }
                android.util.Log.e("PROFILE_VM", "Profile data fetch failed: $errorMsgFull")
                _profileState.value = ProfileState.Error(errorMsgFull)
            }
        }
    }

    fun uploadProfilePicture(uri: Uri) {
        viewModelScope.launch {
            val currentState = _profileState.value
            if (currentState is ProfileState.Success) {
                _profileState.value = ProfileState.Loading
                val uploadResult = mediaRepository.uploadImage(uri)
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
