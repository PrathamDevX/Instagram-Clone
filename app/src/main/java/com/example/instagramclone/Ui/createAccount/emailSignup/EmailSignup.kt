package com.example.instagramclone.Ui.createAccount.emailSignup

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
import com.example.instagramclone.components.createAccount.mobCreation.BackButton
import com.example.instagramclone.components.createAccount.NextButton
import com.example.instagramclone.components.createAccount.emailCreation.ContinueWithMobNo
import com.example.instagramclone.components.createAccount.emailCreation.EmailField

@Composable
fun EmailSignup(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color(0xFF162127))
    ) {

        BackButton(navController)

        EmailHeadline()

        EmailField(navController)

        Spacer(modifier = Modifier.height(10.dp))

        NextButton(navController)

        Spacer(modifier = Modifier.height(12.dp))

        ContinueWithMobNo(navController)

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