package com.example.instagramclone.screen.createAccount.birthdayInput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.components.createAccount.AlreadyAccButton
import com.example.instagramclone.components.createAccount.birthdayInput.BirthdayBackButton
import com.example.instagramclone.components.createAccount.birthdayInput.DateField
import com.example.instagramclone.components.createAccount.passwordCreation.PasswordNextButton

@Composable
fun BirthdayInput(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color(0xFF162127))
    ) {

        BirthdayBackButton(navController)

        BirthdayHeadline()

        DateField()

        Spacer(modifier = Modifier.height(10.dp))

        PasswordNextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            Modifier
                .fillMaxHeight()
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlreadyAccButton(navController)
        }


    }
}