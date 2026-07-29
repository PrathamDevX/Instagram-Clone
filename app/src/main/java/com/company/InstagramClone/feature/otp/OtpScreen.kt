package com.company.InstagramClone.feature.otp

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.otp.components.CodeNotReceivedButton
import com.company.InstagramClone.feature.otp.components.OtpBackButton
import com.company.InstagramClone.feature.otp.components.OtpField
import com.company.InstagramClone.feature.otp.components.OtpHeadline
import com.company.InstagramClone.feature.otp.components.OtpNextButton
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.viewmodel.AuthState
import com.company.InstagramClone.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.PhoneAuthProvider

import com.company.InstagramClone.ui.viewmodel.VerificationType

@Composable
fun Otp(
    navController: NavController,
    type: String?,
    verificationId: String? = null,
    email: String? = null,
    viewModel: AuthViewModel = viewModel()
) {
    var otpCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    val verificationType by viewModel.currentVerificationType.collectAsState()

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (verificationType == VerificationType.Login) {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                } else {
                    val route = Routes.Password.replace("{email}", email ?: "")
                    navController.navigate(route)
                }
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
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
        OtpBackButton(navController)

        OtpHeadline()

        OtpField(
            otpCode = otpCode,
            onOtpChange = { otpCode = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        val isLoading = authState is AuthState.Loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            OtpNextButton {
                if (otpCode.length >= 6) {
                    if (type == "mobile") {
                        val vId = verificationId ?: viewModel.verificationId.value
                        if (vId != null) {
                            val credential = PhoneAuthProvider.getCredential(vId, otpCode)
                            viewModel.signInWithCredential(credential)
                        } else {
                            Toast.makeText(context, "Verification ID missing", Toast.LENGTH_SHORT).show()
                        }
                    } else if (type == "email") {
                        if (viewModel.verifyEmailOtp(otpCode)) {
                            when (verificationType) {
                                VerificationType.Signup -> {
                                    val route = Routes.Password.replace("{email}", email ?: "")
                                    navController.navigate(route)
                                }
                                VerificationType.Login -> {
                                    // Handled in LaunchedEffect
                                }
                                else -> {
                                    val route = Routes.Password.replace("{email}", email ?: "")
                                    navController.navigate(route)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Invalid OTP code", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val route = Routes.Password.replace("{email}", email ?: "")
                        navController.navigate(route)
                    }
                } else {
                    Toast.makeText(context, "Please enter the 6-digit code", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CodeNotReceivedButton(navController)
    }
}
