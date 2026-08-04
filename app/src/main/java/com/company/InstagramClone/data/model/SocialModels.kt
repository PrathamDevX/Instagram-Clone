package com.company.InstagramClone.data.model

import com.google.firebase.Timestamp
import java.util.Date
import androidx.compose.runtime.Immutable

@Immutable
data class PostRecord(
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val caption: String = "",
    val mediaUrls: List<String> = emptyList(),
    val mediaType: String = "image",
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)

@Immutable
data class StoryRecord(
    val storyId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val mediaUrl: String = "",
    val mediaType: String = "image", // "image" or "video"
    val timestamp: Timestamp = Timestamp.now(),
    val expiresAt: Timestamp = Timestamp(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)), // 24 hours later
    val views: Int = 0
)

@Immutable
data class ReelRecord(
    val reelId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val mediaType: String = "video",
    val caption: String = "",
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val commentsCount: Int = 0,
    val viewsCount: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)

@Immutable
data class CommentRecord(
    val commentId: String = "",
    val userId: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
