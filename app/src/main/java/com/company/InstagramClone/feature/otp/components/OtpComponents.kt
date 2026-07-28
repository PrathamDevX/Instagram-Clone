package com.company.InstagramClone.feature.otp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.R
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramButtonStyle
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField

@Composable
fun OtpBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun OtpHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.otp_headline),
        description = stringResource(R.string.otp_description)
    )
}

@Composable
fun OtpField(
    otpCode: String,
    onOtpChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = otpCode,
            onValueChange = { if (it.length <= 6) onOtpChange(it) },
            placeholder = stringResource(R.string.otp_hint)
        )
    }
}

@Composable
fun OtpNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
    )
}

@Composable
fun CodeNotReceivedButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.otp_code_not_received),
        onClick = {
            navController.navigate(Routes.EmailSignup)
        },
        style = InstagramButtonStyle.Secondary
    )
}
