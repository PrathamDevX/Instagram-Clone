package com.company.InstagramClone.data

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val fullName: String = "",
    val username: String = "",
    val birthday: String = "",
    val profileImageUrl: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
