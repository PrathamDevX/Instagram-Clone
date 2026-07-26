package com.example.instagramclone.feature.signup.components

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
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.R
import com.example.instagramclone.ui.components.InstagramBackButton
import com.example.instagramclone.ui.components.InstagramButton
import com.example.instagramclone.ui.components.InstagramButtonStyle
import com.example.instagramclone.ui.components.InstagramHeadline
import com.example.instagramclone.ui.components.InstagramTextField

@Composable
fun EmailBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.navigate(Routes.MobNoSignup)
        }
    )
}

@Composable
fun EmailHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_email_headline),
        description = stringResource(R.string.signup_email_description)
    )
}

@Composable
fun EmailField() {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.signup_email_hint)
        )
    }
}

@Composable
fun EmailNextButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = {
            navController.navigate(Routes.Otp.replace("{type}", "email"))
        }
    )
}

@Composable
fun ContinueWithMobNo(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_email_continue_with_mob),
        onClick = {
            navController.navigate(Routes.MobNoSignup)
        },
        style = InstagramButtonStyle.Secondary
    )
}
