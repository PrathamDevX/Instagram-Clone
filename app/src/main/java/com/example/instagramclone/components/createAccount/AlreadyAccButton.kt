package com.example.instagramclone.components.createAccount

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.ui.theme.InstagramSans

@Composable
fun AlreadyAccButton(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = {}
        ) {
            Text(
                "I already have an account",
                fontFamily = InstagramSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xFF5CA4FA)
            )
        }
    }
}