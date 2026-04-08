package com.example.instagramclone.components.createAccount.birthdayInput

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField() {

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
            OutlinedTextField(
                value = formattedDate,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        "Date of birth",
                        color = Color(0x8BEFEFEF)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFF46474D),
                    disabledTextColor = Color.White
                ),
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
                    color = Color(0xFF1C1C1E),
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
                                Text("CANCEL")
                            }

                            TextButton(
                                onClick = {
                                    selectedDateMillis = datePickerState.selectedDateMillis
                                    showDialog = false
                                }
                            ) {
                                Text("SET")
                            }
                        }
                    }
                }
            }
        }
    }
}


