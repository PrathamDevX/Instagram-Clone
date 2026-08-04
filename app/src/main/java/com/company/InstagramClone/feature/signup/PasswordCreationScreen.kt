package com.company.InstagramClone.feature.signup

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.signup.components.PasswordCreationBackButton
import com.company.InstagramClone.feature.signup.components.PasswordHeadline
import com.company.InstagramClone.feature.signup.components.PasswordField
import com.company.InstagramClone.feature.signup.components.PasswordNextButton
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.viewmodel.AuthState
import com.company.InstagramClone.ui.viewmodel.AuthViewModel

@Composable
fun PasswordCreation(
    navController: NavController,
    email: String?,
    viewModel: AuthViewModel = viewModel()
) {
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate(Routes.Name)
            viewModel.resetState()
        }
    }

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
        PasswordCreationBackButton(navController)

        PasswordHeadline()

        PasswordField(
            password = password,
            onPasswordChange = { password = it },
            navController = navController
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (authState is AuthState.Loading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            PasswordNextButton {
                if (password.length >= 8) {
                    viewModel.signUp(email ?: "", password)
                } else {
                    Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
