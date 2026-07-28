package com.company.InstagramClone.feature.signup.components

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
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.R
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramButtonStyle
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField
import com.company.InstagramClone.ui.components.LearnMoreText

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
    mobNo: String,
    onMobNoChange: (String) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = mobNo,
            onValueChange = onMobNoChange,
            placeholder = stringResource(R.string.signup_mob_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LearnMoreText(navController)
    }
}

@Composable
fun MobNoNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
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
