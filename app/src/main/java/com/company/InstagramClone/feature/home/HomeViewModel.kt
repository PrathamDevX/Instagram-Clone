package com.company.InstagramClone.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val userProfile: UserProfile?,
        val posts: List<Post> = emptyList()
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        refreshHome()
    }

    fun refreshHome() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            android.util.Log.d("HOME_VM", "Refreshing home data...")
            
            val profileResult = repository.getUserProfile()
            val postsResult = repository.getPosts()
            
            if (profileResult.isSuccess && postsResult.isSuccess) {
                _homeState.value = HomeState.Success(
                    userProfile = profileResult.getOrNull(),
                    posts = postsResult.getOrDefault(emptyList())
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
