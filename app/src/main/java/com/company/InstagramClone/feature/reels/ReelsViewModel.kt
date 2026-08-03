package com.company.InstagramClone.feature.reels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.model.CommentRecord
import com.company.InstagramClone.data.model.ReelRecord
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ReelsState {
    object Loading : ReelsState()
    data class Success(
        val reels: List<ReelRecord>,
        val userProfile: UserProfile? = null
    ) : ReelsState()
    data class Error(val message: String) : ReelsState()
}

class ReelsViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : ViewModel() {
    private val _reelsState = MutableStateFlow<ReelsState>(ReelsState.Loading)
    val reelsState = _reelsState.asStateFlow()

    private val _activeComments = MutableStateFlow<List<CommentRecord>>(emptyList())
    val activeComments = _activeComments.asStateFlow()

    init {
        fetchReels()
    }

    fun toggleLike(reelId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val currentState = _reelsState.value
        
        if (currentState is ReelsState.Success) {
            // Optimistic Update
            val updatedReels = currentState.reels.map { reel ->
                if (reel.reelId == reelId) {
                    val newIsLiked = !reel.isLiked
                    reel.copy(
                        isLiked = newIsLiked,
                        likesCount = if (newIsLiked) reel.likesCount + 1 else reel.likesCount - 1
                    )
                } else {
                    reel
                }
            }
            _reelsState.value = currentState.copy(reels = updatedReels)
            
            // Backend Sync
            viewModelScope.launch {
                socialRepository.toggleLike(reelId, false, currentUserId)
            }
        }
    }

    fun fetchComments(reelId: String) {
        viewModelScope.launch {
            socialRepository.getComments(reelId, false)
                .onSuccess { _activeComments.value = it }
        }
    }

    fun addComment(reelId: String, text: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            val profile = repository.getUserProfile().getOrNull()
            val comment = CommentRecord(
                userId = user.uid,
                username = profile?.username ?: user.email?.substringBefore("@") ?: "Anonymous",
                profileImageUrl = profile?.profileImageUrl ?: "",
                text = text
            )
            socialRepository.addComment(reelId, false, comment)
            fetchComments(reelId)
            // Removed fetchReels() to prevent feed jump
        }
    }

    fun toggleFollow(targetUserId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (currentUserId == targetUserId) return
        
        viewModelScope.launch {
            val isFollowingResult = socialRepository.checkIfFollowing(currentUserId, targetUserId)
            val isFollowing = isFollowingResult.getOrDefault(false)
            
            if (isFollowing) {
                socialRepository.unfollowUser(currentUserId, targetUserId)
            } else {
                socialRepository.followUser(currentUserId, targetUserId)
            }
            // No fetchReels() here to prevent full page reload
            // In a future update, we can add isFollowing to ReelRecord for UI state
        }
    }

    fun fetchReels() {
        viewModelScope.launch {
            _reelsState.value = ReelsState.Loading
            val profile = repository.getUserProfile().getOrNull()
            socialRepository.getAllReels()
                .onSuccess { reels ->
                    _reelsState.value = ReelsState.Success(reels, profile)
                }
                .onFailure {
                    _reelsState.value = ReelsState.Error(it.message ?: "Failed to load reels")
                }
        }
    }
}
