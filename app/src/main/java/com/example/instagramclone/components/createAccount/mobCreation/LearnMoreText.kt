package com.example.instagramclone.components.createAccount.mobCreation

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.Navigation.Routes

@Composable
fun LearnMoreText(
    navController: NavController,
) {
    val annotatedText = buildAnnotatedString {
        append("You may receive WhatsApp and SMS notifications from us. ")

        pushStringAnnotation(
            tag = "LEARN_MORE",
            annotation = "Learn more"
        )
        withStyle(
            style = SpanStyle(
                color = Color(0xFF2A96F6),
                fontSize = 14.sp
            )
        ){
            append("Learn more")
        }
        pop()
    }
    ClickableText(
        text = annotatedText,
        style = TextStyle(
            color = Color(0x8BEFEFEF),
            fontSize = 14.sp
        ),
        onClick = {offset ->
            annotatedText.getStringAnnotations(
                start = offset,
                end = offset,
                tag = "LEARN_MORE"
            ).firstOrNull()?.let {
                navController.navigate(Routes.LearnMore)
            }

        }
    )
}