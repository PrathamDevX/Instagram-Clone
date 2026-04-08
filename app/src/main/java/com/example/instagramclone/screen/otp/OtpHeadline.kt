package com.example.instagramclone.screen.otp

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
fun OtpHeadline() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "Enter the confirmation code",
            color = Color.White,
            fontSize = 26.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "To confirm your profile, enter the 6-digit code that we've sent via WhatsApp",
            color = Color(0xEEEFEFEF),
            fontSize = 17.sp,
            fontFamily = InstagramSans,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
        )
    }
}