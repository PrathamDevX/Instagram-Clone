package com.example.instagramclone.feature.home

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
import com.example.instagramclone.feature.home.components.HomeBottomNavigation
import com.example.instagramclone.feature.home.components.HomeTopBar
import com.example.instagramclone.feature.home.components.PostItem
import com.example.instagramclone.feature.home.components.StoriesSection
import com.example.instagramclone.ui.theme.InstagramBlack
import com.example.instagramclone.ui.theme.InstagramBorder

@Composable
fun Home(
    navController: NavController
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            HomeBottomNavigation()
        },
        containerColor = InstagramBlack
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(InstagramBlack)
        ) {
            item {
                StoriesSection()
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
