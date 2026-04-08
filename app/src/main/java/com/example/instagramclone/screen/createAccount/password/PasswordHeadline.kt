package com.example.instagramclone.screen.createAccount.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.ui.theme.InstagramSans

@Composable
fun PasswordHeadline() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "Create a password",
            color = Color.White,
            fontSize = 26.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Create a password with at least six letters or numbers. It should be something that others can't guess.",
            color = Color(0xEEEFEFEF),
            fontSize = 17.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )
    }
}