package com.example.instagramclone.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.ui.theme.InstagramLightBlue
import com.example.instagramclone.ui.theme.InstagramSoftWhite

@Composable
fun LearnMoreText(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val prefix = stringResource(R.string.signup_mob_learn_more_prefix)
    val link = stringResource(R.string.signup_mob_learn_more_link)
    
    val annotatedText = buildAnnotatedString {
        append(prefix)

        pushStringAnnotation(
            tag = "LEARN_MORE",
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
        modifier = modifier,
        style = TextStyle(
            color = InstagramSoftWhite,
            fontSize = 14.sp
        ),
        onClick = { offset ->
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
