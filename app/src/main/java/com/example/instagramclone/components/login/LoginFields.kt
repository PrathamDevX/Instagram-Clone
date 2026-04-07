package com.example.instagramclone.components.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginInputFields() {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    "Username, email address or Mobile numb..",
                    color = Color(0xFFBEC2D5),
                    maxLines = 1,
                    fontSize = 15.sp
                )},
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF46474D),
                focusedBorderColor = Color.White,

                cursorColor = Color.White,

                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { password = it },
            label = {
                Text(
                    "Password",
                    color = Color(0xFFBEC2D5),
                    maxLines = 1,
                    fontSize = 15.sp
                )},
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF46474D),
                focusedBorderColor = Color.White,

                cursorColor = Color.White,

                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )
    }
}