package com.example.instagramclone.components.createAccount

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.Navigation.Routes
import java.security.DrbgParameters

@Composable
fun NextButton(
    navController: NavController
) {
    Button(
        onClick = {
            navController.navigate(Routes.Otp)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0064E0)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
            .height(45.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            "Next",
            fontSize = 15.sp,
            color = Color.White
        )
    }
}