package com.example.instagramclone.components.createAccount.birthdayInput

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.Navigation.Routes

@Composable
fun BirthdayBackButton(
    navController: NavController
) {
    IconButton(
        onClick = {
            navController.navigate(Routes.Otp)
        },
        colors = IconButtonDefaults.iconButtonColors(Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}