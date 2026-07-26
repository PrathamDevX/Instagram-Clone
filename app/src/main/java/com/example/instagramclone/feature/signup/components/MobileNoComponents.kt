package com.example.instagramclone.feature.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.instagramclone.ui.components.LearnMoreText

@Composable
fun MobNoBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.navigate(Routes.Login)
        }
    )
}

@Composable
fun MobNoHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_mob_headline),
        description = stringResource(R.string.signup_mob_description)
    )
}

@Composable
fun MobNoField(
    navController: NavController
) {
    var mobNo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = mobNo,
            onValueChange = { mobNo = it },
            placeholder = stringResource(R.string.signup_mob_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LearnMoreText(navController)
    }
}

@Composable
fun MobNoNextButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = {
            navController.navigate(Routes.Otp.replace("{type}", "mobile"))
        }
    )
}

@Composable
fun ContinueWithEmailButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_mob_continue_with_email),
        onClick = {
            navController.navigate(Routes.EmailSignup)
        },
        style = InstagramButtonStyle.Secondary
    )
}
