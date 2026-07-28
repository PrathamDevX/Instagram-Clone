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
    data class Success(val userProfile: UserProfile?) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            repository.getUserProfile()
                .onSuccess { profile ->
                    _homeState.value = HomeState.Success(profile)
                }
                .onFailure { error ->
                    _homeState.value = HomeState.Error(error.message ?: "Failed to load profile")
                }
        }
    }

    fun logout() {
        repository.signOut()
    }
}
