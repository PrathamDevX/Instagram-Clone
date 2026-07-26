package com.example.instagramclone.feature.login.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.R
import com.example.instagramclone.ui.components.InstagramButton
import com.example.instagramclone.ui.components.InstagramButtonStyle
import com.example.instagramclone.ui.components.InstagramPasswordTextField
import com.example.instagramclone.ui.components.InstagramTextField
import com.example.instagramclone.ui.theme.*

@Composable
fun CloseButton() {
    val context = LocalContext.current
    IconButton(
        onClick = {
            (context as? Activity)?.finish()
        },
        colors = IconButtonDefaults.iconButtonColors(Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun LanguageSelector() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.clickable { /* action */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.login_language_selector),
                color = InstagramHintGrey,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = InstagramHintGrey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun InstagramIcon() {
    Image(
        painter = painterResource(id = R.drawable.instagrami_icon),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(65.dp)
    )
}

@Composable
fun LoginInputFields() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = stringResource(R.string.login_username_hint)
        )

        Spacer(modifier = Modifier.height(12.dp))

        InstagramPasswordTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(R.string.login_password_hint)
        )
    }
}

@Composable
fun LoginButton(navController: NavController) {
    InstagramButton(
        text = stringResource(R.string.login_button),
        onClick = { navController.navigate(Routes.Home) }
    )
}

@Composable
fun ForgetButton() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = {}) {
            Text(
                text = stringResource(R.string.login_forgot_password),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CreateNewAccButton(navController: NavController) {
    InstagramButton(
        text = stringResource(R.string.login_create_new_account),
        onClick = { navController.navigate(Routes.MobNoSignup) },
        style = InstagramButtonStyle.Outlined
    )
}

@Composable
fun Meta() {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            text = " " + stringResource(R.string.login_meta),
            color = Color.White,
            fontSize = 17.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium
        )
    }
}
