package com.example.instagramclone.ui.theme

import com.example.instagramclone.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//)

val InstagramSans = FontFamily(
    Font(R.font.instagram_sans_regular, FontWeight.Normal),
    Font(R.font.instagram_sans_medium, FontWeight.Medium),
    Font(R.font.instagram_sans_bold, FontWeight.Bold),
    Font(R.font.instagram_sans_light, FontWeight.Light)
)

val Billabong = FontFamily(Font(R.font.billabong, FontWeight.Normal))

val InstagramHeadline = FontFamily(
    Font(R.font.instagram_sans_headline, FontWeight.Normal)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = InstagramSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = InstagramSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = InstagramSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)