package com.company.InstagramClone.feature.findYourAcc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.R
import com.company.InstagramClone.ui.components.InstagramBackButton
import com.company.InstagramClone.ui.components.InstagramButton
import com.company.InstagramClone.ui.components.InstagramHeadline
import com.company.InstagramClone.ui.components.InstagramTextField
import com.company.InstagramClone.ui.theme.*

@Composable
fun FindYourAccBackButton(
    navController: NavController
) {
    InstagramBackButton(
        onClick = {
            navController.popBackStack()
        }
    )
}

@Composable
fun FindYourAccHeadline() {
    InstagramHeadline(
        title = stringResource(R.string.find_acc_headline),
        description = stringResource(R.string.find_acc_description)
    )
}

@Composable
fun FindYourAccTextField(
    navController: NavController
) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        InstagramTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = stringResource(R.string.find_acc_hint)
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccHackedTextButton(navController)
    }
}

@Composable
fun FindYourAccNextButton(
    navController: NavController
) {
    InstagramButton(
        text = stringResource(R.string.signup_next),
        onClick = {
            // Logic to find account would go here
        }
    )
}

@Composable
fun AccHackedTextButton(
    navController: NavController,
) {
    val prefix = stringResource(R.string.find_acc_cant_reset_prefix)
    val link = stringResource(R.string.find_acc_hacked_link)
    
    val annotatedText = buildAnnotatedString {
        append(prefix)

        pushStringAnnotation(
            tag = "ACC_HACKED",
            annotation = link
        )
        withStyle(
            style = SpanStyle(
                color = InstagramLightBlue,
                fontSize = 14.sp
            )
        ){
            append(link)
        }
        pop()
    }
    ClickableText(
        text = annotatedText,
        style = TextStyle(
            color = InstagramSoftWhite,
            fontSize = 14.sp
        ),
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                start = offset,
                end = offset,
                tag = "ACC_HACKED"
            ).firstOrNull()?.let {
                navController.navigate(Routes.LearnMore)
            }
        }
    )
}

@Composable
fun AccHackedScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = stringResource(R.string.coming_soon),
            color = Color.White
        )
    }
}
