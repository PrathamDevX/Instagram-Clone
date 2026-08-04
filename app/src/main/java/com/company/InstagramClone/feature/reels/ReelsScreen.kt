package com.company.InstagramClone.feature.reels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.company.InstagramClone.R
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.feature.home.components.HomeBottomNavigation
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.components.VideoPlayer
import com.company.InstagramClone.ui.theme.InstagramSans
import com.company.InstagramClone.utils.CloudinaryHelper
import com.company.InstagramClone.ui.components.CommentBottomSheet
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun ReelsScreen(
    navController: NavController,
    viewModel: ReelsViewModel = viewModel()
) {
    val reelsState by viewModel.reelsState.collectAsStateWithLifecycle()
    val comments by viewModel.activeComments.collectAsStateWithLifecycle()
    val optimisticLikes by viewModel.optimisticLikes.collectAsStateWithLifecycle()
    var showCommentsForReelId by remember { mutableStateOf<String?>(null) }
    
    val reels = viewModel.reelsPagingData.collectAsLazyPagingItems()
    
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        bottomBar = {
            val userProfile = (reelsState as? ReelsState.Success)?.userProfile
            HomeBottomNavigation(
                selectedRoute = Routes.Reels,
                onHomeClick = { navController.navigate(Routes.Home) },
                onProfileClick = { navController.navigate(Routes.Profile) },
                onReelsClick = { /* Already here */ },
                profileImageUrl = userProfile?.profileImageUrl ?: ""
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (reelsState) {
                is ReelsState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is ReelsState.Success -> {
                    val listState = rememberLazyListState()
                    
                    val currentlyPlayingIndex by remember {
                        derivedStateOf {
                            val info = listState.layoutInfo
                            val visible = info.visibleItemsInfo
                            if (visible.isEmpty()) return@derivedStateOf -1

                            val center = (info.viewportStartOffset + info.viewportEndOffset) / 2
                            
                            var closestIndex = -1
                            var minDistance = Int.MAX_VALUE

                            for (item in visible) {
                                val itemCenter = item.offset + (item.size / 2)
                                val distance = kotlin.math.abs(itemCenter - center)
                                if (distance < minDistance) {
                                    minDistance = distance
                                    closestIndex = item.index
                                }
                            }
                            closestIndex
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = reels.itemCount,
                            key = reels.itemKey { it.reelId }
                        ) { index ->
                            val reel = reels[index]
                            if (reel != null) {
                                val likeState = optimisticLikes[reel.reelId]
                                ReelItem(
                                    reel = if (likeState != null) reel.copy(isLiked = likeState.first, likesCount = likeState.second) else reel,
                                    height = screenHeight,
                                    shouldPlay = currentlyPlayingIndex == index,
                                    onLikeClick = { viewModel.toggleLike(reel.reelId, reel.likesCount, reel.isLiked) },
                                    onCommentClick = {
                                        viewModel.fetchComments(reel.reelId)
                                        showCommentsForReelId = reel.reelId
                                    },
                                    onFollowClick = { viewModel.toggleFollow(reel.userId) },
                                    onUserClick = { userId ->
                                        navController.navigate(Routes.Profile.replace("{userId}", userId))
                                    }
                                )
                            }
                        }
                    }
                }
                is ReelsState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = (reelsState as ReelsState.Error).message, color = Color.White)
                    }
                }
            }

            if (showCommentsForReelId != null) {
                CommentBottomSheet(
                    comments = comments,
                    onAddComment = { text ->
                        showCommentsForReelId?.let { viewModel.addComment(it, text) }
                    },
                    onDismiss = { showCommentsForReelId = null }
                )
            }

            // Top Camera Icon
            IconButton(
                onClick = { navController.navigate(Routes.CreateMedia) },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).statusBarsPadding()
            ) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReelItem(
    reel: ReelRecord,
    height: androidx.compose.ui.unit.Dp,
    shouldPlay: Boolean = true,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxWidth().height(height)) {
        if (shouldPlay) {
            VideoPlayer(
                videoUrl = CloudinaryHelper.getOptimizedVideoUrl(reel.videoUrl),
                modifier = Modifier.fillMaxSize(),
                shouldPlay = true
            )
        } else {
            GlideImage(
                model = CloudinaryHelper.getThumbnailUrl(reel.videoUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                it.thumbnail(0.1f)
            }
        }

        // Overlay UI (Left side info)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth(0.8f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onUserClick(reel.userId) }
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    if (reel.profileImageUrl.isNotEmpty()) {
                        GlideImage(
                            model = reel.profileImageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = reel.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = InstagramSans
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                        .clickable { onFollowClick() }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = "Follow", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = reel.caption,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = InstagramSans,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${reel.username} · Original audio", color = Color.White, fontSize = 13.sp)
            }
        }

        // Side Buttons (Right side actions)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ReelSideAction(
                icon = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = reel.likesCount.toString(),
                onClick = onLikeClick,
                iconColor = if (reel.isLiked) Color.Red else Color.White
            )
            ReelSideAction(
                icon = painterResource(id = R.drawable.chat),
                label = reel.commentsCount.toString(),
                onClick = onCommentClick
            )
            ReelSideAction(icon = painterResource(id = R.drawable.send), label = "")
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            ) {
                if (reel.profileImageUrl.isNotEmpty()) {
                    GlideImage(
                        model = reel.profileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ReelSideAction(
    icon: Any,
    label: String,
    onClick: () -> Unit = {},
    iconColor: Color = Color.White
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(30.dp)
            )
            is androidx.compose.ui.graphics.painter.Painter -> Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        if (label.isNotEmpty()) {
            Text(text = label, color = Color.White, fontSize = 12.sp)
        }
    }
}
