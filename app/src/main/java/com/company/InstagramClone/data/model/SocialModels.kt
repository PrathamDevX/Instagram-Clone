package com.company.InstagramClone.data.model

import com.google.firebase.Timestamp

data class PostRecord(
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val caption: String = "",
    val mediaUrls: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)

data class StoryRecord(
    val storyId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val mediaUrl: String = "",
    val mediaType: String = "image", // "image" or "video"
    val timestamp: Timestamp = Timestamp.now(),
    val expiresAt: Timestamp = Timestamp.now(),
    val views: Int = 0
)

data class ReelRecord(
    val reelId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val caption: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)
