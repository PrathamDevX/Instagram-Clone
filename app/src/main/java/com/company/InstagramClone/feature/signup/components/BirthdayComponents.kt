package com.company.InstagramClone.feature.signup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.R
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField
import com.company.InstagramClone.ui.theme.InstagramDialogGrey
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BirthdayBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun BirthdayHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.signup_birthday_headline),
        description = stringResource(R.string.signup_birthday_description)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDateField(
    formattedDate: String,
    onDateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onDateClick() }
        ) {
            InstagramTextField(
                value = formattedDate,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                placeholder = stringResource(R.string.signup_birthday_hint),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BirthdayNextButton(
    onClick: () -> Unit
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = onClick
    )
}
