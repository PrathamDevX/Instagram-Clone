package com.company.InstagramClone.feature.reels

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.company.InstagramClone.data.model.ReelRecord
import com.company.InstagramClone.feature.home.components.HomeBottomNavigation
import com.company.InstagramClone.feature.reels.components.DoubleTapHeartAnimation
import com.company.InstagramClone.feature.reels.components.ReelActions
import com.company.InstagramClone.feature.reels.components.ReelDescription
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.components.VideoPlayer
import com.company.InstagramClone.ui.components.CommentBottomSheet
import com.company.InstagramClone.utils.PlayerPoolManager
import kotlinx.coroutines.delay

@Composable
fun ReelsScreen(
    navController: NavController,
    viewModel: ReelsViewModel = viewModel()
) {
    val reels = viewModel.reelsPagingData.collectAsLazyPagingItems()
    val comments by viewModel.activeComments.collectAsStateWithLifecycle()
    val optimisticLikes by viewModel.optimisticLikes.collectAsStateWithLifecycle()
    var showCommentsForReelId by remember { mutableStateOf<String?>(null) }
    
    val pagerState = rememberPagerState(pageCount = { reels.itemCount })

    // Handle Playback & Pre-warming
    LaunchedEffect(pagerState.currentPage) {
        if (reels.itemCount > 0) {
            viewModel.onPageSelected(pagerState.currentPage, reels)
        }
    }

    Scaffold(
        bottomBar = {
            val state = reelsState(viewModel)
            val userProfile = if (state is ReelsState.Success) state.userProfile else null
            
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
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { index ->
                val reel = reels[index]
                if (reel != null) {
                    val likeState = optimisticLikes[reel.reelId]
                    ReelFullItem(
                        reel = if (likeState != null) reel.copy(isLiked = likeState.first, likesCount = likeState.second) else reel,
                        shouldPlay = pagerState.currentPage == index,
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

            // Top Bar Overlay
            IconButton(
                onClick = { navController.navigate(Routes.CreateMedia) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .statusBarsPadding()
            ) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White)
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
        }
    }
}

@Composable
private fun reelsState(viewModel: ReelsViewModel) = viewModel.reelsState.collectAsStateWithLifecycle().value

@Composable
fun ReelFullItem(
    reel: ReelRecord,
    shouldPlay: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onFollowClick: () -> Unit,
    onUserClick: (String) -> Unit
) {
    var isLikedTrigger by remember { mutableStateOf(false) }
    var isPausedByTap by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { isPausedByTap = !isPausedByTap },
                    onDoubleTap = {
                        isLikedTrigger = true
                        onLikeClick()
                    },
                    onLongPress = { isPausedByTap = true },
                    onPress = {
                        try {
                            awaitRelease()
                        } finally {
                            isPausedByTap = false
                        }
                    }
                )
            }
    ) {
        VideoPlayer(
            videoUrl = reel.videoUrl,
            modifier = Modifier.fillMaxSize(),
            shouldPlay = shouldPlay && !isPausedByTap
        )

        // UI Overlays
        Box(modifier = Modifier.fillMaxSize()) {
            ReelDescription(
                reel = reel,
                onFollowClick = onFollowClick,
                onUserClick = { onUserClick(reel.userId) },
                modifier = Modifier.align(Alignment.BottomStart)
            )

            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                ReelActions(
                    reel = reel,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick,
                    onShareClick = {}
                )
            }
        }

        DoubleTapHeartAnimation(
            trigger = isLikedTrigger,
            onAnimationEnd = { isLikedTrigger = false }
        )
    }
}
