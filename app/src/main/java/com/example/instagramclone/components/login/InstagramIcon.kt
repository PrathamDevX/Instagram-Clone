package com.example.instagramclone.components.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.instagramclone.R

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


