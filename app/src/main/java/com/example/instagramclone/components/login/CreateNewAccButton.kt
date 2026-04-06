package com.example.instagramclone.components.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateNewAccButton() {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFF488CCC)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        Text(
            "Create new account",
            color = Color(0xFF488CCC)
        )
    }
}