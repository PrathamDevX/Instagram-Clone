package com.company.InstagramClone.feature.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramPasswordTextField
import com.company.InstagramClone.ui.components.LearnMoreText

@Composable
fun PasswordCreationBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun PasswordHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_password_headline),
        description = stringResource(R.string.signup_password_description)
    )
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramPasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.signup_password_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LearnMoreText(navController)
    }
}

@Composable
fun PasswordNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
    )
}
