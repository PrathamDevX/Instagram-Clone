package com.example.instagramclone.feature.signup.components

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
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.ui.components.InstagramBackButton
import com.example.instagramclone.ui.components.InstagramButton
import com.example.instagramclone.ui.components.InstagramHeadline
import com.example.instagramclone.ui.components.InstagramTextField
import com.example.instagramclone.ui.components.LearnMoreText

@Composable
fun NameBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun NameHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_name_headline),
        description = stringResource(R.string.signup_name_description)
    )
}

@Composable
fun FullNameField(
    navController: NavController
) {
    var fullName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = stringResource(R.string.signup_name_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LearnMoreText(navController)
    }
}

@Composable
fun NameNextButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = {
            navController.navigate(Routes.Username)
        }
    )
}
