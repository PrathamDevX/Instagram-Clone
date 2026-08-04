package com.company.InstagramClone.feature.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.home.components.HomeBottomNavigation
import com.company.InstagramClone.feature.profile.components.*
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.theme.InstagramBlack

@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String? = null, // Optional UID to view someone else
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        viewModel.fetchProfileData(userId)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.uploadProfilePicture(context, uri)
        }
    }

    Scaffold(
        topBar = {
            val username = (profileState as? ProfileState.Success)?.userProfile?.username ?: ""
            ProfileTopBar(
                username = username,
                onMenuClick = {
                    viewModel.logout()
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        },
        bottomBar = {
            val userProfile = (profileState as? ProfileState.Success)?.userProfile
            HomeBottomNavigation(
                selectedRoute = Routes.Profile,
                onHomeClick = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                },
                onProfileClick = {
                    // Navigate to self if viewing someone else
                    if (userId != null) {
                        navController.navigate("profile")
                    }
                },
                onReelsClick = {
                    navController.navigate(Routes.Reels)
                },
                profileImageUrl = userProfile?.profileImageUrl ?: ""
            )
        },
        containerColor = InstagramBlack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(InstagramBlack)
        ) {
            when (profileState) {
                is ProfileState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is ProfileState.Success -> {
                    val data = profileState as ProfileState.Success
                    var selectedTabIndex by remember { mutableIntStateOf(0) }
                    
                    ProfileHeader(
                        userProfile = data.userProfile,
                        postCount = data.userPosts.size,
                        onImageClick = {
                            if (data.isCurrentUser) {
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        }
                    )
                    
                    ProfileActions(
                        isCurrentUser = data.isCurrentUser,
                        isFollowing = data.isFollowing,
                        onFollowClick = { viewModel.toggleFollow() }
                    )
                    
                    HighlightsSection()
                    
                    ProfileTabs(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )
                    
                    when (selectedTabIndex) {
                        0 -> {
                            PostsGrid(
                                posts = data.userPosts,
                                onPostClick = { post ->
                                    val route = Routes.PostDetail
                                        .replace("{userId}", post.userId)
                                        .replace("{postId}", post.id.toString())
                                    navController.navigate(route)
                                }
                            )
                        }
                        1 -> {
                            ReelsGrid(
                                reels = data.userReels,
                                onReelClick = { reel ->
                                    navController.navigate(Routes.Reels)
                                }
                            )
                        }
                        else -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No tags yet", color = Color.White)
                            }
                        }
                    }
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (profileState as ProfileState.Error).message,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Button(
                            onClick = { viewModel.fetchProfileData(userId) },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = com.company.InstagramClone.ui.theme.InstagramLink
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
