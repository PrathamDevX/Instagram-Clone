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
import com.example.instagramclone.feature.signup.components.ContinueWithEmailButton
import com.example.instagramclone.feature.signup.components.MobNoBackButton
import com.example.instagramclone.feature.signup.components.MobNoField
import com.example.instagramclone.feature.signup.components.MobNoHeadline
import com.example.instagramclone.feature.signup.components.MobNoNextButton
import com.example.instagramclone.ui.theme.InstagramBlack

@Composable
fun MobNoSignupScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {

        MobNoBackButton(navController)

        MobNoHeadline()

        MobNoField(navController)

        Spacer(modifier = Modifier.height(10.dp))

        MobNoNextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

        ContinueWithEmailButton(navController)

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
