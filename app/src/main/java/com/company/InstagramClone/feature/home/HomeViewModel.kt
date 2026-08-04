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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.company.InstagramClone.data.paging.PostPagingSource

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val userProfile: UserProfile?,
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

    // Local cache for optimistic likes to prevent list reloads
    private val _optimisticLikes = MutableStateFlow<Map<String, Pair<Boolean, Int>>>(emptyMap())
    val optimisticLikes = _optimisticLikes.asStateFlow()

    val postsPagingData: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 2),
        pagingSourceFactory = { PostPagingSource(socialRepository, FirebaseAuth.getInstance().currentUser?.uid) }
    ).flow.cachedIn(viewModelScope)

    init {
        refreshHome()
    }

    fun toggleLike(postId: String, currentLikes: Int, currentlyLiked: Boolean) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        
        // Optimistic Update
        val newIsLiked = !currentlyLiked
        val newLikesCount = if (newIsLiked) currentLikes + 1 else currentLikes - 1
        _optimisticLikes.value = _optimisticLikes.value + (postId to Pair(newIsLiked, newLikesCount))
        
        viewModelScope.launch {
            socialRepository.toggleLike(postId, true, currentUserId)
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
        }
    }

    fun refreshHome() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val profileResult = repository.getUserProfile()
            val storiesResult = socialRepository.getActiveStories()
            
            if (profileResult.isSuccess) {
                _homeState.value = HomeState.Success(
                    userProfile = profileResult.getOrNull(),
                    stories = storiesResult.getOrDefault(emptyList())
                )
            } else {
                _homeState.value = HomeState.Error(profileResult.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        repository.signOut()
    }
}
