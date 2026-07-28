package com.company.InstagramClone.feature.signup

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.company.InstagramClone.feature.signup.components.*
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.viewmodel.AuthViewModel

@Composable
fun EmailSignupScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {
        EmailBackButton(navController)

        EmailHeadline()

        EmailField(
            email = email,
            onEmailChange = { email = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        EmailNextButton {
            viewModel.updateSignupData { it.copy(email = email) }
            viewModel.generateAndSendOtp(email)
            val route = Routes.Otp
                .replace("{type}", "email")
                .replace("{id}", "")
                .replace("{email}", email)
            navController.navigate(route)
        }

        Spacer(modifier = Modifier.height(12.dp))

        ContinueWithMobNo(navController)

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
