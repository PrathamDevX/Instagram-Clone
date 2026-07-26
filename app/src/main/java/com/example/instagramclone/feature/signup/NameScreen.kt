package com.example.instagramclone.feature.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.feature.signup.components.AlreadyAccButton
import com.example.instagramclone.feature.signup.components.FullNameField
import com.example.instagramclone.feature.signup.components.NameBackButton
import com.example.instagramclone.feature.signup.components.NameHeadline
import com.example.instagramclone.feature.signup.components.NameNextButton
import com.example.instagramclone.ui.theme.InstagramBlack

@Composable
fun NameScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {
        NameBackButton(navController)

        NameHeadline()

        FullNameField(navController)

        Spacer(modifier = Modifier.height(10.dp))

        NameNextButton(navController)

        Column(
            Modifier
                .fillMaxHeight()
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlreadyAccButton(navController)
        }
    }
}
