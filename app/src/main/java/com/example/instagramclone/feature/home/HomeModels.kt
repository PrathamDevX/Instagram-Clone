package com.example.instagramclone.feature.home

data class Story(
    val id: Int,
    val username: String,
    val imageUrl: String,
    val isLive: Boolean = false
)

data class Post(
    val id: Int,
    val username: String,
    val userImageUrl: String,
    val postImageUrl: String,
    val caption: String,
    val likesCount: Int,
    val timeAgo: String
)

val dummyStories = listOf(
    Story(1, "Your story", ""),
    Story(2, "____khush____", "", isLive = true),
    Story(3, "ankitframez", ""),
    Story(4, "themiddleground", ""),
    Story(5, "someone_else", ""),
    Story(6, "another_user", "")
)

val dummyPosts = listOf(
    Post(
        id = 1,
        username = "lifecomesinphases",
        userImageUrl = "",
        postImageUrl = "",
        caption = "The thing about being broke is you're all in it together 🥺",
        likesCount = 1234,
        timeAgo = "2 hours ago"
    ),
    Post(
        id = 2,
        username = "tech_explorer",
        userImageUrl = "",
        postImageUrl = "",
        caption = "Exploring the new features of Android 15! 🚀 #AndroidDev #Kotlin",
        likesCount = 856,
        timeAgo = "5 hours ago"
    )
)
