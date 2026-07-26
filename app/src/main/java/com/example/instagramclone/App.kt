package com.example.instagramclone

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.feature.signup.PasswordCreation
import com.example.instagramclone.feature.signup.MobNoSignupScreen
import com.example.instagramclone.feature.login.LoginScreen
import com.example.instagramclone.navigation.Routes
import com.example.instagramclone.feature.learnmore.LearnMoreScreen
import com.example.instagramclone.feature.findYourAcc.FindYourAccScreen
import com.example.instagramclone.feature.otp.Otp
import com.example.instagramclone.feature.home.Home
import com.example.instagramclone.feature.signup.BirthdayInputScreen
import com.example.instagramclone.feature.signup.EmailSignupScreen
import com.example.instagramclone.feature.signup.NameScreen
import com.example.instagramclone.feature.signup.UsernameScreen

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ){
        composable(Routes.Home){
            Home(navController)
        }

        composable(Routes.Login){
            LoginScreen(navController)
        }

        composable(Routes.MobNoSignup){
            MobNoSignupScreen(navController)
        }

        composable(Routes.LearnMore){
            LearnMoreScreen(navController)
        }

        composable(Routes.Otp){ backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            Otp(navController, type)
        }

        composable(Routes.EmailSignup) {
            EmailSignupScreen(navController)
        }

        composable(Routes.Name) {
            NameScreen(navController)
        }

        composable(Routes.Username) {
            UsernameScreen(navController)
        }

        composable(Routes.BirthdayInput) {
            BirthdayInputScreen(navController)
        }

        composable(Routes.Password) {
            PasswordCreation(navController)
        }

        composable(Routes.FindYourAcc){
            FindYourAccScreen(navController)
        }
    }
}
