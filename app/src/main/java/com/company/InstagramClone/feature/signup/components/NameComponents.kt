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
import com.company.InstagramClone.R
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField
import com.company.InstagramClone.ui.components.LearnMoreText

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
    fullName: String,
    onFullNameChange: (String) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = stringResource(R.string.signup_name_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LearnMoreText(navController)
    }
}

@Composable
fun NameNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
    )
}
