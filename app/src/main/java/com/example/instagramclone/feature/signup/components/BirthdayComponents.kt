package com.example.instagramclone.feature.signup.components

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
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.R
import com.example.instagramclone.ui.components.InstagramBackButton
import com.example.instagramclone.ui.components.InstagramButton
import com.example.instagramclone.ui.components.InstagramHeadline
import com.example.instagramclone.ui.components.InstagramTextField
import com.example.instagramclone.ui.theme.InstagramDialogGrey
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
fun BirthdayDateField() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    val formattedDate = selectedDateMillis?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(it))
    } ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { showDialog = true }
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

        if (showDialog) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDateMillis
            )

            Dialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = InstagramDialogGrey,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        DatePicker(state = datePickerState)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = stringResource(R.string.cancel))
                            }
                            TextButton(
                                onClick = {
                                    selectedDateMillis = datePickerState.selectedDateMillis
                                    showDialog = false
                                }
                            ) {
                                Text(text = stringResource(R.string.set))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BirthdayNextButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = {
            navController.navigate(Routes.Name)
        }
    )
}
