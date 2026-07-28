package com.company.InstagramClone.feature.findYourAcc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.company.InstagramClone.feature.findYourAcc.components.FindYourAccBackButton
import com.company.InstagramClone.feature.findYourAcc.components.FindYourAccHeadline
import com.company.InstagramClone.feature.findYourAcc.components.FindYourAccNextButton
import com.company.InstagramClone.feature.findYourAcc.components.FindYourAccTextField
import com.company.InstagramClone.ui.theme.InstagramBlack

@Composable
fun FindYourAccScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(InstagramBlack)
    ) {
        FindYourAccBackButton(navController)

        FindYourAccHeadline()

        FindYourAccTextField(navController)

        Spacer(modifier = Modifier.height(10.dp))

        FindYourAccNextButton(navController)
    }
}
