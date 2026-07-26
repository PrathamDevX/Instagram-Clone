package com.example.instagramclone.feature.otp

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.feature.otp.components.CodeNotReceivedButton
import com.example.instagramclone.feature.otp.components.OtpBackButton
import com.example.instagramclone.feature.otp.components.OtpField
import com.example.instagramclone.feature.otp.components.OtpHeadline
import com.example.instagramclone.feature.otp.components.OtpNextButton
import com.example.instagramclone.ui.theme.InstagramBlack

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
            .background(InstagramBlack)
    ) {
        OtpBackButton(navController)

        OtpHeadline()

        OtpField()

        Spacer(modifier = Modifier.height(10.dp))

        OtpNextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

        CodeNotReceivedButton(navController)
    }
}
