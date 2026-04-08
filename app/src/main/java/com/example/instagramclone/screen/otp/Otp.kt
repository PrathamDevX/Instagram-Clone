package com.example.instagramclone.screen.otp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.components.otp.CodeNotReceivedButton
import com.example.instagramclone.components.otp.OtpBackButton
import com.example.instagramclone.components.otp.OtpNextButton

@Composable
fun Otp(
    navController: NavController,
    type: String?
) {

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color(0xFF162127))
    ) {
        OtpBackButton(navController)

        OtpHeadline()

        Spacer(modifier = Modifier.height(10.dp))

        OtpNextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

        CodeNotReceivedButton(navController)


    }
}