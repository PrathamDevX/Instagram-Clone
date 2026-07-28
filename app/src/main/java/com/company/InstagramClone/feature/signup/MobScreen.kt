package com.company.InstagramClone.feature.signup

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.company.InstagramClone.feature.signup.components.*
import com.company.InstagramClone.ui.theme.InstagramBlack
import com.company.InstagramClone.ui.viewmodel.AuthViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.company.InstagramClone.navigation.Routes

@Composable
fun MobNoSignupScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var mobNo by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as Activity

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("PHONE_AUTH", "Verification completed automatically")
            viewModel.signInWithCredential(credential)
            // Note: Navigation to Password will be handled by the authState observer in OtpScreen or here
            // But if it's auto-verified here, we should navigate from this screen
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("PHONE_AUTH", "Verification failed: ${e.message}", e)
            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d("PHONE_AUTH", "Code sent: $verificationId")
            viewModel.setVerificationId(verificationId)
            val route = Routes.Otp
                .replace("{type}", "mobile")
                .replace("{id}", verificationId)
                .replace("{email}", "")
            navController.navigate(route)
        }
    }

    val authState by viewModel.authState.collectAsState()
    LaunchedEffect(authState) {
        if (authState is com.company.InstagramClone.ui.viewmodel.AuthState.Authenticated) {
            navController.navigate(Routes.Password + "?email=")
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {
        MobNoBackButton(navController)

        MobNoHeadline()

        MobNoField(
            mobNo = mobNo,
            onMobNoChange = { mobNo = it },
            navController = navController
        )

        Spacer(modifier = Modifier.height(10.dp))

        MobNoNextButton {
            if (mobNo.startsWith("+") && mobNo.length >= 10) {
                viewModel.updateSignupData { it.copy(phoneNumber = mobNo) }
                viewModel.verifyPhoneNumber(mobNo, activity, callbacks)
            } else {
                Toast.makeText(context, "Please enter phone number with country code (e.g. +91...)", Toast.LENGTH_LONG).show()
            }
        }

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
