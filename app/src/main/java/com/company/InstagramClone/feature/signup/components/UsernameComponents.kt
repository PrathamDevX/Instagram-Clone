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
import com.company.InstagramClone.R
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField

@Composable
fun UsernameBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun UsernameHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_username_headline),
        description = stringResource(R.string.signup_username_description)
    )
}

@Composable
fun UsernameField(
    username: String,
    onUsernameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = username,
            onValueChange = onUsernameChange,
            placeholder = stringResource(R.string.signup_username_hint)
        )
    }
}

@Composable
fun UsernameNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
    )
}
