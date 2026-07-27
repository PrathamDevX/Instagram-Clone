package com.example.instagramclone.feature.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.feature.home.Post
import com.example.instagramclone.feature.home.Story
import com.example.instagramclone.feature.home.dummyStories
import com.example.instagramclone.ui.theme.Billabong
import com.example.instagramclone.ui.theme.InstagramBlack
import com.example.instagramclone.ui.theme.InstagramHeadline
import com.example.instagramclone.ui.theme.InstagramSans

@Composable
fun HomeTopBar() {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )

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
fun StoriesSection() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dummyStories) { story ->
            StoryItem(story)
        }
    }
}

@Composable
fun StoryItem(story: Story) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
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
                        if (story.username != "Your story") {
                            Modifier.border(2.dp, storyGradient, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                // Placeholder for profile image
            }

            if (story.username == "Your story") {
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
            text = story.username,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = InstagramSans
        )
    }
}

@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = post.username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = InstagramSans
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.username} · Original audio",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = InstagramSans
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White
            )
        }

        // Post Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.DarkGray)
        )

        // Interaction Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "Comment",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(25
                        .dp)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "Bookmark",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Likes and Caption
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = "${post.likesCount} likes",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = InstagramSans
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = post.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = InstagramSans
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = post.caption,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = InstagramSans
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.timeAgo,
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = InstagramSans
            )
        }
    }
}

@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = InstagramBlack,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(painter = painterResource(id = R.drawable.house), contentDescription = "Home", modifier = Modifier.size(28.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(id = R.drawable.play), contentDescription = "Reels", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(id = R.drawable.send), contentDescription = "Post", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search", modifier = Modifier.size(25.dp)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { 
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}
