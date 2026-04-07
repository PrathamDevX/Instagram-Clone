package com.example.instagramclone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.Ui.createAccount.MobNolSignup.MobNoSignup
import com.example.instagramclone.Ui.login.Login
import com.example.instagramclone.Navigation.Routes
import com.example.instagramclone.Ui.LearnMore.LearnMore
import com.example.instagramclone.Ui.createAccount.emailSignup.EmailSignup
import com.example.instagramclone.Ui.createAccount.Otp
import com.example.instagramclone.Ui.home.Home

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

        composable(Routes.Otp){
            Otp(navController)
        }

        composable(Routes.EmailSignup) {
            EmailSignup(navController)
        }
    }
}