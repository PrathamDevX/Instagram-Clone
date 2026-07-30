package com.company.InstagramClone.feature.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.login.components.*
import com.company.InstagramClone.ui.theme.InstagramDarkGrey
import com.company.InstagramClone.ui.viewmodel.AuthState
import com.company.InstagramClone.ui.viewmodel.AuthViewModel
import com.company.InstagramClone.ui.viewmodel.VerificationType
import com.company.InstagramClone.navigation.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Login) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.OtpSent -> {
                val route = Routes.Otp
                    .replace("{type}", "email")
                    .replace("{id}", "")
                    .replace("{email}", email)
                navController.navigate(route)
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramDarkGrey)
            .statusBarsPadding()
    ) {
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

            ForgetButton {
                if (email.isNotEmpty()) {
                    viewModel.resetPassword(email)
                    Toast.makeText(context, "Password reset email sent. Please check your inbox.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Please enter your email first", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            CreateNewAccButton(navController)

            Spacer(modifier = Modifier.height(12.dp))

            Meta()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
