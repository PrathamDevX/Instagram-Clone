package com.company.InstagramClone.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.model.CommentRecord
import com.company.InstagramClone.data.model.StoryRecord
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val userProfile: UserProfile?,
        val posts: List<Post> = emptyList(),
        val stories: List<StoryRecord> = emptyList()
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    private val _activeComments = MutableStateFlow<List<CommentRecord>>(emptyList())
    val activeComments = _activeComments.asStateFlow()

    init {
        refreshHome()
    }

    fun toggleLike(postId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentState = _homeState.value
        
        if (currentState is HomeState.Success) {
            // Optimistic Update
            val updatedPosts = currentState.posts.map { post ->
                if (post.postId == postId) {
                    val newIsLiked = !post.isLiked
                    post.copy(
                        isLiked = newIsLiked,
                        likesCount = if (newIsLiked) post.likesCount + 1 else post.likesCount - 1
                    )
                } else {
                    post
                }
            }
            _homeState.value = currentState.copy(posts = updatedPosts)
            
            // Backend Sync
            viewModelScope.launch {
                val result = socialRepository.toggleLike(postId, true, currentUserId)
                if (result.isFailure) {
                    // Revert on failure if needed, or just let next refresh fix it
                    android.util.Log.e("HOME_VM", "Failed to sync like to server")
                }
            }
        }
    }

    fun fetchComments(postId: String) {
        viewModelScope.launch {
            socialRepository.getComments(postId, true)
                .onSuccess { _activeComments.value = it }
        }
    }

    fun addComment(postId: String, text: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            val profile = repository.getUserProfile().getOrNull()
            val comment = CommentRecord(
                userId = user.uid,
                username = profile?.username ?: user.email?.substringBefore("@") ?: "Anonymous",
                profileImageUrl = profile?.profileImageUrl ?: "",
                text = text
            )
            socialRepository.addComment(postId, true, comment)
            fetchComments(postId)
            // Removed refreshHome() to prevent feed jump
        }
    }

    fun refreshHome() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            android.util.Log.d("HOME_VM", "Refreshing home data...")
            
            val profileResult = repository.getUserProfile()
            val postsResult = repository.getPosts()
            val storiesResult = socialRepository.getActiveStories()
            
            if (profileResult.isSuccess && postsResult.isSuccess) {
                _homeState.value = HomeState.Success(
                    userProfile = profileResult.getOrNull(),
                    posts = postsResult.getOrDefault(emptyList()),
                    stories = storiesResult.getOrDefault(emptyList())
                )
            } else {
                val rawError = profileResult.exceptionOrNull()?.message 
                    ?: postsResult.exceptionOrNull()?.message 
                    ?: "Unknown error"
                
                val errorMsg = if (rawError.contains("offline", ignoreCase = true)) {
                    "The app is offline. Please check your internet connection and ensure Firestore is enabled in the Firebase Console."
                } else {
                    rawError
                }
                
                android.util.Log.e("HOME_VM", "Home refresh failed: $errorMsg")
                _homeState.value = HomeState.Error(errorMsg)
            }
        }
    }

    fun logout() {
        repository.signOut()
    }
}
