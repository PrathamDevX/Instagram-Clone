package com.company.InstagramClone.feature.signup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.R
import com.company.InstagramClone.ui.theme.InstagramLink
import com.company.InstagramClone.ui.theme.InstagramSans

@Composable
fun AlreadyAccButton(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = {
                navController.navigate(Routes.FindYourAcc)
            }
        ) {
            Text(
                text = stringResource(R.string.signup_already_have_account),
                fontFamily = InstagramSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = InstagramLink
            )
        }
    }
}
