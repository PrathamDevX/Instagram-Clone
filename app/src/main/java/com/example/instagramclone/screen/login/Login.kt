package com.example.instagramclone.screen.login


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.components.login.CloseButton
import com.example.instagramclone.components.login.CreateNewAccButton
import com.example.instagramclone.components.login.ForgetButton
import com.example.instagramclone.components.login.InstagramIcon
import com.example.instagramclone.components.login.LanguageSelector
import com.example.instagramclone.components.login.LoginButton
import com.example.instagramclone.components.login.LoginInputFields
import com.example.instagramclone.components.login.Meta

@Composable
fun Login(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff1f1f21))
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp)
        ) {
            CloseButton()

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector()
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InstagramIcon()

            Spacer(modifier = Modifier.height(38.dp))

            LoginInputFields()

            LoginButton(navController)

            Spacer(modifier = Modifier.height(10.dp))

            ForgetButton()
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CreateNewAccButton(navController)

            Spacer(modifier = Modifier.height(12.dp))

            Meta()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



