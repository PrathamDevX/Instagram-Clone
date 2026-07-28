package com.company.InstagramClone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.InstagramClone.ui.theme.InstagramBlue
import com.company.InstagramClone.ui.theme.InstagramButtonSecondary
import com.company.InstagramClone.ui.theme.InstagramLink
import com.company.InstagramClone.ui.theme.InstagramTextGrey

enum class InstagramButtonStyle {
    Primary, Secondary, Outlined
}

@Composable
fun InstagramButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: InstagramButtonStyle = InstagramButtonStyle.Primary
) {
    when (style) {
        InstagramButtonStyle.Primary, InstagramButtonStyle.Secondary -> {
            val containerColor = if (style == InstagramButtonStyle.Primary) InstagramBlue else InstagramButtonSecondary
            val contentColor = if (style == InstagramButtonStyle.Primary) Color.White else InstagramTextGrey

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 15.sp
                )
            }
        }
        InstagramButtonStyle.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, InstagramLink),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(45.dp)
            ) {
                Text(
                    text = text,
                    color = InstagramLink,
                    fontSize = 15.sp
                )
            }
        }
    }
}
