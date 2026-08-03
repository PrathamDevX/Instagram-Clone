package com.company.InstagramClone.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.company.InstagramClone.R
import com.company.InstagramClone.feature.home.Post
import com.company.InstagramClone.feature.home.Story
import com.company.InstagramClone.data.model.StoryRecord
import com.company.InstagramClone.ui.components.VideoPlayer
import com.company.InstagramClone.utils.CloudinaryHelper
import com.company.InstagramClone.ui.theme.Billabong
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.theme.InstagramSans

@Composable
fun HomeTopBar(onAddClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Instagram",
            fontFamily = Billabong,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Activity",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun StoriesSection(
    currentUsername: String = "Your story",
    currentUserProfilePic: String = "",
    stories: List<StoryRecord> = emptyList(),
    onStoryClick: (StoryRecord) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            StoryItem(
                story = Story(0, currentUsername, currentUserProfilePic),
                isCurrent = true
            )
        }
        
        items(stories) { storyRecord ->
            StoryItem(
                storyRecord = storyRecord,
                onClick = { onStoryClick(storyRecord) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryItem(
    story: Story? = null,
    storyRecord: StoryRecord? = null,
    isCurrent: Boolean = false,
    onClick: () -> Unit = {}
) {
    val username = story?.username ?: storyRecord?.username ?: ""
    val imageUrl = story?.imageUrl ?: storyRecord?.profileImageUrl ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp).clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .padding(3.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val storyGradient = Brush.linearGradient(
                colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCAF45))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (!isCurrent) {
                            Modifier.border(2.dp, storyGradient, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                if (imageUrl.isNotEmpty()) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (isCurrent) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0095F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = username,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = InstagramSans
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostItem(
    post: Post,
    shouldPlay: Boolean = true,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onUserClick(post.userId) }
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    if (post.userImageUrl.isNotEmpty()) {
                        GlideImage(
                            model = CloudinaryHelper.getOptimizedUrl(post.userImageUrl, 100, 100),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = post.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = InstagramSans
                )
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Post Media Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { onLikeClick() })
                }
        ) {
            if (post.postImageUrl.isNotEmpty()) {
                val inferredType = CloudinaryHelper.getMediaType(post.postImageUrl, post.mediaType)
                
                if (inferredType == "video") {
                    VideoPlayer(
                        videoUrl = CloudinaryHelper.getOptimizedVideoUrl(post.postImageUrl),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.8f),
                        shouldPlay = shouldPlay
                    )
                } else {
                    GlideImage(
                        model = CloudinaryHelper.getFeedUrl(post.postImageUrl),
                        contentDescription = "Post Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        it.thumbnail(0.1f)
                    }
                }
            }
        }

        // Interaction Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = onCommentClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "Comment",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Bookmark",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Likes and Caption
        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
            Text(
                text = "${post.likesCount} likes",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                fontFamily = InstagramSans
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Text(
                    text = post.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = InstagramSans
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = post.caption,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontFamily = InstagramSans
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.timeAgo,
                color = Color.Gray,
                fontSize = 11.sp,
                fontFamily = InstagramSans
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeBottomNavigation(
    selectedRoute: String = com.company.InstagramClone.navigation.Routes.Home,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onReelsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    profileImageUrl: String = ""
) {
    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = InstagramBlack,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedRoute == com.company.InstagramClone.navigation.Routes.Home,
            onClick = onHomeClick,
            icon = { Icon(painter = painterResource(id = R.drawable.house), contentDescription = "Home", modifier = Modifier.size(28.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onSearchClick,
            icon = { Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onAddClick,
            icon = { Icon(painter = painterResource(id = R.drawable.send), contentDescription = "Post", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == com.company.InstagramClone.navigation.Routes.Reels,
            onClick = onReelsClick,
            icon = { Icon(painter = painterResource(id = R.drawable.play), contentDescription = "Reels", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == com.company.InstagramClone.navigation.Routes.Profile,
            onClick = onProfileClick,
            icon = { 
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .then(
                            if (selectedRoute == com.company.InstagramClone.navigation.Routes.Profile) {
                                Modifier.border(1.5.dp, Color.White, CircleShape)
                            } else {
                                Modifier
                            }
                        )
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    if (profileImageUrl.isNotEmpty()) {
                        GlideImage(
                            model = profileImageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}
