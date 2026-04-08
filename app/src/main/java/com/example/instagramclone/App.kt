package com.example.instagramclone

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.screen.createAccount.MobNolSignup.MobNoSignup
import com.example.instagramclone.screen.login.Login
import com.example.instagramclone.Navigation.Routes
import com.example.instagramclone.screen.LearnMore.LearnMore
import com.example.instagramclone.screen.createAccount.birthdayInput.BirthdayInput
import com.example.instagramclone.screen.createAccount.emailSignup.EmailSignup
import com.example.instagramclone.screen.otp.Otp
import com.example.instagramclone.screen.createAccount.password.PasswordCreation
import com.example.instagramclone.screen.home.Home

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login
    ){

        composable (Routes.Home){
            Home(navController)
        }

        composable (Routes.Login){
            Login(navController)
        }

        composable(Routes.MobNoSignup){
            MobNoSignup(navController)
        }

        composable(Routes.LearnMore){
            LearnMore(navController)
        }

        composable(Routes.Otp){backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            Otp(navController, type)
        }

        composable(Routes.EmailSignup) {
            EmailSignup(navController)
        }

        composable(Routes.Password){
            PasswordCreation(navController)
        }

        composable(Routes.BirthdayInput) {
            BirthdayInput(navController)
        }
    }
}