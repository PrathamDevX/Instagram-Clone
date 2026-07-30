package com.company.InstagramClone.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.InstagramClone.navigation.Routes

@Composable
fun Home(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeState by homeViewModel.homeState.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onAddClick = {
                    navController.navigate(Routes.CreateMedia)
                }
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                selectedRoute = Routes.Home,
                onHomeClick = {
                    // Already on Home, maybe scroll to top
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile)
                }
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
                val posts = data.posts
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(InstagramBlack)
                ) {
                    item {
                        StoriesSection(userProfile?.username ?: "Your story")
                    }
                    
                    item {
                        HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
                    }

                    items(posts) { post ->
                        PostItem(post)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
