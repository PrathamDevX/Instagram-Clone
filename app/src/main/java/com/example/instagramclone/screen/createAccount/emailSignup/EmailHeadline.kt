package com.example.instagramclone.screen.createAccount.emailSignup

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
fun EmailHeadline() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "What's your email address?",
            color = Color.White,
            fontSize = 26.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Enter the email at which you can be contacted. No one will see this on your profile.",
            color = Color(0xEEEFEFEF),
            fontSize = 17.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )
    }
}