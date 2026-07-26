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
import com.example.instagramclone.feature.signup.components.BirthdayBackButton
import com.example.instagramclone.feature.signup.components.BirthdayDateField
import com.example.instagramclone.feature.signup.components.BirthdayHeadline
import com.example.instagramclone.feature.signup.components.BirthdayNextButton
import com.example.instagramclone.ui.theme.InstagramBlack

@Composable
fun BirthdayInputScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {

        BirthdayBackButton(navController)

        BirthdayHeadline()

        BirthdayDateField()

        Spacer(modifier = Modifier.height(10.dp))

        BirthdayNextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

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
