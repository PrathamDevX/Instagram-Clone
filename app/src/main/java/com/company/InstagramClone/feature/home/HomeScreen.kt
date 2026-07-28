package com.company.InstagramClone.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                onLogoutClick = {
                    homeViewModel.logout()
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        },
        bottomBar = {
            HomeBottomNavigation()
        },
        containerColor = InstagramBlack
    ) { paddingValues ->
        val userProfile = (homeState as? HomeState.Success)?.userProfile
        
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

            items(dummyPosts) { post ->
                PostItem(post)
            }
        }
    }
}
