package com.company.InstagramClone.feature.signup.components

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
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.signup_email_hint)
        )
    }
}

@Composable
fun EmailNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
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
