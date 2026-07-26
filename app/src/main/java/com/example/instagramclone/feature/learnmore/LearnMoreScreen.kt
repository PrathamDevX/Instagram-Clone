package com.example.instagramclone.feature.learnmore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.InstagramBlack

@Composable
fun LearnMoreScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = stringResource(R.string.coming_soon),
            color = Color.White
        )
    }
}
