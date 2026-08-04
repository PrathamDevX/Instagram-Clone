package com.company.InstagramClone.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.InstagramClone.ui.theme.InstagramLink
import androidx.navigation.NavController
import com.company.InstagramClone.feature.home.components.HomeBottomNavigation
import com.company.InstagramClone.feature.home.components.HomeTopBar
import com.company.InstagramClone.feature.home.components.PostItem
import com.company.InstagramClone.feature.home.components.StoriesSection
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.theme.InstagramBorder

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.InstagramClone.ui.components.CommentBottomSheet
import com.company.InstagramClone.navigation.Routes
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

@Composable
fun Home(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val comments by homeViewModel.activeComments.collectAsStateWithLifecycle()
    val optimisticLikes by homeViewModel.optimisticLikes.collectAsStateWithLifecycle()
    var showCommentsForPostId by remember { mutableStateOf<String?>(null) }
    
    val posts = homeViewModel.postsPagingData.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopBar(
                onAddClick = {
                    navController.navigate(Routes.CreateMedia)
                }
            )
        },
        bottomBar = {
            val userProfile = (homeState as? HomeState.Success)?.userProfile
            HomeBottomNavigation(
                selectedRoute = Routes.Home,
                onHomeClick = {
                    // Already on Home, maybe scroll to top
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile)
                },
                onReelsClick = {
                    navController.navigate(Routes.Reels)
                },
                profileImageUrl = userProfile?.profileImageUrl ?: ""
            )
        },
        containerColor = InstagramBlack
    ) { paddingValues ->
        when (homeState) {
            is HomeState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(InstagramBlack),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is HomeState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(InstagramBlack),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (homeState as HomeState.Error).message,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { homeViewModel.refreshHome() },
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramLink)
                    ) {
                        Text("Retry")
                    }
                }
            }
            is HomeState.Success -> {
                val data = homeState as HomeState.Success
                val userProfile = data.userProfile
                val stories = data.stories
                
                val listState = rememberLazyListState()
                
                val currentlyPlayingIndex by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItems = layoutInfo.visibleItemsInfo
                        if (visibleItems.isEmpty()) return@derivedStateOf -1

                        val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                        
                        var closestIndex = -1
                        var minDistance = Int.MAX_VALUE

                        for (item in visibleItems) {
                            if (item.index < 2) continue // Skip stories and divider
                            
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(InstagramBlack)
                ) {
                    item(key = "stories", contentType = "header") {
                        StoriesSection(
                            currentUsername = userProfile?.username ?: "Your story",
                            currentUserProfilePic = userProfile?.profileImageUrl ?: "",
                            stories = stories,
                            onStoryClick = { story ->
                                navController.navigate(Routes.StoryViewer.replace("{userId}", story.userId))
                            }
                        )
                    }
                    
                    item(key = "divider", contentType = "header") {
                        HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
                    }

                    items(
                        count = posts.itemCount,
                        key = posts.itemKey { it.postId },
                        contentType = posts.itemContentType { "post" }
                    ) { index ->
                        val post = posts[index]
                        if (post != null) {
                            val postListIndex = index + 2 
                            val likeState = optimisticLikes[post.postId]
                            
                            PostItem(
                                post = if (likeState != null) post.copy(isLiked = likeState.first, likesCount = likeState.second) else post,
                                shouldPlay = currentlyPlayingIndex == postListIndex,
                                onLikeClick = { homeViewModel.toggleLike(post.postId, post.likesCount, post.isLiked) },
                                onCommentClick = {
                                    homeViewModel.fetchComments(post.postId)
                                    showCommentsForPostId = post.postId
                                },
                                onUserClick = { userId ->
                                    navController.navigate(Routes.Profile.replace("{userId}", userId))
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        if (showCommentsForPostId != null) {
            CommentBottomSheet(
                comments = comments,
                onAddComment = { text ->
                    showCommentsForPostId?.let { homeViewModel.addComment(it, text) }
                },
                onDismiss = { showCommentsForPostId = null }
            )
        }
    }
}
