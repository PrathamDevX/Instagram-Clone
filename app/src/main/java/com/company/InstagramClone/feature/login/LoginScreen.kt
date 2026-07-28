package com.company.InstagramClone.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.login.components.*
import com.company.InstagramClone.ui.theme.InstagramDarkGrey
import com.company.InstagramClone.ui.viewmodel.AuthState
import com.company.InstagramClone.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    if (authState is AuthState.Authenticated) {
        navController.navigate(com.company.InstagramClone.navigation.Routes.Home) {
            popUpTo(com.company.InstagramClone.navigation.Routes.Login) { inclusive = true }
        }
        viewModel.resetState()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramDarkGrey)
            .statusBarsPadding()
    ) {
        // ... (CloseButton and LanguageSelector remains the same)
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp)
        ) {
            CloseButton()

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector()
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InstagramIcon()

            Spacer(modifier = Modifier.height(38.dp))

            LoginInputFields(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it }
            )

            LoginButton(navController) {
                viewModel.signIn(email, password)
            }

            Spacer(modifier = Modifier.height(10.dp))

            ForgetButton()

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreateNewAccButton(navController)

            Spacer(modifier = Modifier.height(12.dp))

            Meta()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
