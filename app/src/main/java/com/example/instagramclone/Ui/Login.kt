package com.example.instagramclone.Ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.components.login.CloseButton
import com.example.instagramclone.components.login.CreateNewAccButton
import com.example.instagramclone.components.login.ForgetButton
import com.example.instagramclone.components.login.InstagramIcon
import com.example.instagramclone.components.login.LanguageSelector
import com.example.instagramclone.components.login.LoginButton
import com.example.instagramclone.components.login.LoginInputFields
import com.example.instagramclone.components.login.Meta
import java.nio.file.WatchEvent

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

        // 🔝 TOP SECTION
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

        // 🔥 CENTER SECTION
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InstagramIcon()

            Spacer(modifier = Modifier.height(38.dp))

            LoginInputFields()

            LoginButton()

            Spacer(modifier = Modifier.height(10.dp))

            ForgetButton()
        }

        // 🔻 BOTTOM SECTION
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CreateNewAccButton()

            Spacer(modifier = Modifier.height(12.dp))

            Meta()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



