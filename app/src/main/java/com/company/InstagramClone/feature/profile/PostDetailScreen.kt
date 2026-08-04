package com.company.InstagramClone.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.feature.home.components.PostItem
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.theme.InstagramSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    userId: String,
    postId: String,
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val posts = (profileState as ProfileState.Success).userPosts
            val index = posts.indexOfFirst { it.id.toString() == postId }
            if (index != -1) {
                listState.scrollToItem(index)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (profileState as? ProfileState.Success)?.userProfile?.username?.uppercase() ?: "POSTS",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = InstagramSans
                        )
                        Text(
                            text = "Posts",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = InstagramSans
                        )
                    }
                },
                navigationIcon = {
                    InstagramBackButton(onClick = { navController.popBackStack() })
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = InstagramBlack
    ) { paddingValues ->
        when (profileState) {
            is ProfileState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ProfileState.Success -> {
                val posts = (profileState as ProfileState.Success).userPosts
                
                val currentlyPlayingIndex by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItems = layoutInfo.visibleItemsInfo
                        if (visibleItems.isEmpty()) return@derivedStateOf -1

                        val viewPortCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                        
                        visibleItems.minByOrNull { item ->
                            val itemCenter = item.offset + (item.size / 2)
                            kotlin.math.abs(itemCenter - viewPortCenter)
                        }?.index ?: -1
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(InstagramBlack)
                ) {
                    itemsIndexed(posts) { index, post ->
                        PostItem(
                            post = post,
                            shouldPlay = currentlyPlayingIndex == index,
                            onUserClick = { userId ->
                                if (userId != (profileState as? ProfileState.Success)?.userProfile?.uid) {
                                    navController.navigate(Routes.Profile.replace("{userId}", userId))
                                }
                            }
                        )
                    }
                }
            }
            is ProfileState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = (profileState as ProfileState.Error).message, color = Color.White)
                }
            }
        }
    }
}
