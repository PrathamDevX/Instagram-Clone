package com.company.InstagramClone.feature.reels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.SocialRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.model.CommentRecord
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.utils.PlayerPoolManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.company.InstagramClone.data.paging.ReelPagingSource
import android.app.Application
import androidx.lifecycle.AndroidViewModel

sealed class ReelsState {
    object Loading : ReelsState()
    data class Success(
        val userProfile: UserProfile? = null
    ) : ReelsState()
    data class Error(val message: String) : ReelsState()
}

class ReelsViewModel(
    application: Application,
    private val repository: AuthRepository = FirebaseAuthRepository(),
    private val socialRepository: SocialRepository = SocialRepository()
) : AndroidViewModel(application) {
    private val _reelsState = MutableStateFlow<ReelsState>(ReelsState.Loading)
    val reelsState = _reelsState.asStateFlow()

    private val _activeComments = MutableStateFlow<List<CommentRecord>>(emptyList())
    val activeComments = _activeComments.asStateFlow()

    private val _optimisticLikes = MutableStateFlow<Map<String, Pair<Boolean, Int>>>(emptyMap())
    val optimisticLikes = _optimisticLikes.asStateFlow()

    private val playerPool = PlayerPoolManager.getInstance(application)

    val reelsPagingData: Flow<PagingData<ReelRecord>> = Pager(
        config = PagingConfig(pageSize = 5, prefetchDistance = 2),
        pagingSourceFactory = { ReelPagingSource(socialRepository, FirebaseAuth.getInstance().currentUser?.uid) }
    ).flow.cachedIn(viewModelScope)

    init {
        fetchReels()
    }

    fun onPageSelected(index: Int, reels: LazyPagingItems<ReelRecord>) {
        // Pre-warm next 2 reels
        viewModelScope.launch {
            for (i in 1..2) {
                val nextIndex = index + i
                if (nextIndex < reels.itemCount) {
                    reels[nextIndex]?.let { reel ->
                        playerPool.prewarm(reel.reelId, reel.videoUrl)
                    }
                }
            }
        }
    }

    fun toggleLike(reelId: String, currentLikes: Int, currentlyLiked: Boolean) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newIsLiked = !currentlyLiked
        val newLikesCount = if (newIsLiked) currentLikes + 1 else currentLikes - 1
        _optimisticLikes.value = _optimisticLikes.value + (reelId to Pair(newIsLiked, newLikesCount))
        
        viewModelScope.launch {
            socialRepository.toggleLike(reelId, false, currentUserId)
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
        }
    }

    fun toggleFollow(targetUserId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (currentUserId == targetUserId) return
        
        viewModelScope.launch {
            val isFollowing = socialRepository.checkIfFollowing(currentUserId, targetUserId).getOrDefault(false)
            if (isFollowing) {
                socialRepository.unfollowUser(currentUserId, targetUserId)
            } else {
                socialRepository.followUser(currentUserId, targetUserId)
            }
        }
    }

    fun fetchReels() {
        viewModelScope.launch {
            _reelsState.value = ReelsState.Loading
            val profile = repository.getUserProfile().getOrNull()
            _reelsState.value = ReelsState.Success(profile)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerPool.releaseAll()
    }
}
