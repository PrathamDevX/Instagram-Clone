package com.company.InstagramClone.feature.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.uploadProfilePicture(uri)
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
            HomeBottomNavigation(
                selectedRoute = Routes.Profile,
                onHomeClick = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                },
                onProfileClick = {
                    // Already on profile
                }
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
                    val profile = data.userProfile
                    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                    
                    ProfileHeader(
                        displayName = profile?.fullName ?: currentUser?.email?.substringBefore("@") ?: "Anonymous",
                        username = profile?.username ?: currentUser?.email ?: "",
                        profileImageUrl = profile?.profileImageUrl ?: "",
                        postCount = data.userPosts.size,
                        followersCount = data.followersCount,
                        followingCount = data.followingCount,
                        onImageClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                    
                    ProfileActions()
                    
                    HighlightsSection()
                    
                    ProfileTabs()
                    
                    PostsGrid(posts = data.userPosts)
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Text(
                            text = (profileState as ProfileState.Error).message,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Button(
                            onClick = { viewModel.fetchProfileData() },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = com.company.InstagramClone.ui.theme.InstagramLink
                            )
                        ) {
                            androidx.compose.material3.Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
