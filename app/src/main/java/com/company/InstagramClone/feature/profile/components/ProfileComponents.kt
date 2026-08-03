package com.company.InstagramClone.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.AssignmentInd
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.company.InstagramClone.R
import com.company.InstagramClone.feature.home.Post
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.utils.CloudinaryHelper
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.theme.InstagramButtonSecondary
import com.company.InstagramClone.ui.theme.InstagramSans

@Composable
fun ProfileTopBar(
    username: String,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = username,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = InstagramSans
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileHeader(
    userProfile: UserProfile?,
    postCount: Int,
    onImageClick: () -> Unit = {}
) {
    val displayName = userProfile?.fullName ?: ""
    val username = userProfile?.username ?: ""
    val profileImageUrl = userProfile?.profileImageUrl ?: ""
    val followersCount = userProfile?.followersCount ?: 0
    val followingCount = userProfile?.followingCount ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile Image
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.clickable { onImageClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    if (profileImageUrl.isNotEmpty()) {
                        GlideImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .size(24.dp)
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
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Stats
            StatItem(label = "posts", count = postCount)
            StatItem(label = "followers", count = followersCount)
            StatItem(label = "following", count = followingCount)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = displayName.ifEmpty { "Anonymous" },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = InstagramSans
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(InstagramButtonSecondary)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AlternateEmail,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = username,
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = InstagramSans
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "+ Add",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InstagramSans
            )
        }
    }
}

@Composable
fun StatItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = InstagramSans
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = InstagramSans
        )
    }
}

@Composable
fun ProfileActions(
    isCurrentUser: Boolean = true,
    isFollowing: Boolean = false,
    onFollowClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isCurrentUser) {
            ProfileButton(text = "Edit profile", modifier = Modifier.weight(1f))
            ProfileButton(text = "Share profile", modifier = Modifier.weight(1f))
        } else {
            ProfileButton(
                text = if (isFollowing) "Following" else "Follow",
                modifier = Modifier.weight(1f),
                onClick = onFollowClick,
                isPrimary = !isFollowing,
                trailingIcon = if (isFollowing) Icons.Default.KeyboardArrowDown else null
            )
            ProfileButton(text = "Message", modifier = Modifier.weight(1f))
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(InstagramButtonSecondary)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(InstagramButtonSecondary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ProfileButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isPrimary: Boolean = false,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) Color(0xFF0095F6) else InstagramButtonSecondary,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(35.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = InstagramSans)
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = trailingIcon, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun HighlightsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "New", color = Color.White, fontSize = 12.sp, fontFamily = InstagramSans)
        }
    }
}

@Composable
fun ProfileTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = InstagramBlack,
        contentColor = Color.White,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Color.White
            )
        }
    ) {
        Tab(selected = selectedTabIndex == 0, onClick = { onTabSelected(0) }) {
            Icon(
                imageVector = Icons.Outlined.GridView,
                contentDescription = null,
                modifier = Modifier.size(28.dp).padding(vertical = 2.dp)
            )
        }
        Tab(selected = selectedTabIndex == 1, onClick = { onTabSelected(1) }) {
            Icon(
                painter = painterResource(id = R.drawable.play),
                contentDescription = null,
                modifier = Modifier.size(28.dp).padding(vertical = 2.dp)
            )
        }
        Tab(selected = selectedTabIndex == 2, onClick = { onTabSelected(2) }) {
            Icon(
                imageVector = Icons.Outlined.AssignmentInd,
                contentDescription = null,
                modifier = Modifier.size(28.dp).padding(vertical = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReelsGrid(
    reels: List<ReelRecord>,
    onReelClick: (ReelRecord) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(reels) { reel ->
            Box(
                modifier = Modifier
                    .aspectRatio(0.6f)
                    .background(Color.DarkGray)
                    .clickable { onReelClick(reel) }
            ) {
                if (reel.videoUrl.isNotEmpty()) {
                    GlideImage(
                        model = CloudinaryHelper.getThumbnailUrl(reel.videoUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        it.thumbnail(0.1f)
                    }
                }
                
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostsGrid(
    posts: List<Post>,
    onPostClick: (Post) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(posts) { post ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color.DarkGray)
                    .clickable { onPostClick(post) }
            ) {
                if (post.postImageUrl.isNotEmpty()) {
                    GlideImage(
                        model = CloudinaryHelper.getThumbnailUrl(post.postImageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        it.thumbnail(0.1f)
                    }
                }
            }
        }
    }
}
