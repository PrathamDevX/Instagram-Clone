package com.company.InstagramClone.feature.home

data class Story(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val isLive: Boolean = false
)

data class Post(
    val id: Int = 0,
    val userId: String = "",
    val username: String = "",
    val userImageUrl: String = "",
    val postImageUrl: String = "",
    val caption: String = "",
    val likesCount: Int = 0,
    val timeAgo: String = ""
)
