package com.example.instagramclone.components.login

import android.health.connect.datatypes.ExercisePerformanceGoal
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R

@Composable
fun Meta() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.meta_icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(19.dp)
        )

        Text(
            " Meta",
            color = Color.White,
            fontSize = 17.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium
        )
    }
}