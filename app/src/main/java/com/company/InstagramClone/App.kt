package com.company.InstagramClone

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.InstagramClone.feature.signup.PasswordCreation
import com.company.InstagramClone.feature.signup.MobNoSignupScreen
import com.company.InstagramClone.feature.login.LoginScreen
import com.company.InstagramClone.navigation.Routes
import com.company.InstagramClone.feature.learnmore.LearnMoreScreen
import com.company.InstagramClone.feature.findYourAcc.FindYourAccScreen
import com.company.InstagramClone.feature.otp.Otp
import com.company.InstagramClone.feature.home.Home
import com.company.InstagramClone.feature.signup.BirthdayInputScreen
import com.company.InstagramClone.feature.signup.EmailSignupScreen
import com.company.InstagramClone.feature.signup.NameScreen
import com.company.InstagramClone.feature.signup.UsernameScreen
import com.company.InstagramClone.feature.profile.ProfileScreen
import com.company.InstagramClone.feature.create.CreateMediaScreen

import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.InstagramClone.ui.viewmodel.AuthViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Login
    ){
        composable(Routes.Home){
            Home(navController)
        }

        composable(Routes.Login){
            LoginScreen(navController, authViewModel)
        }

        composable(Routes.MobNoSignup){
            MobNoSignupScreen(navController, authViewModel)
        }

        composable(Routes.LearnMore){
            LearnMoreScreen(navController)
        }

        composable(
            route = Routes.Otp,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("id") { defaultValue = ""; type = NavType.StringType; nullable = true },
                navArgument("email") { defaultValue = ""; type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val id = backStackEntry.arguments?.getString("id")
            val email = backStackEntry.arguments?.getString("email")
            Otp(navController, type, id, email, authViewModel)
        }

        composable(Routes.EmailSignup) {
            EmailSignupScreen(navController, authViewModel)
        }

        composable(Routes.Name) {
            NameScreen(navController, authViewModel)
        }

        composable(Routes.Username) {
            UsernameScreen(navController, authViewModel)
        }

        composable(Routes.BirthdayInput) {
            BirthdayInputScreen(navController, authViewModel)
        }

        composable(
            route = Routes.Password,
            arguments = listOf(
                navArgument("email") { defaultValue = ""; type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            PasswordCreation(navController, email, authViewModel)
        }

        composable(Routes.FindYourAcc){
            FindYourAccScreen(navController)
        }

        composable(Routes.Profile) {
            ProfileScreen(navController)
        }

        composable(Routes.CreateMedia) {
            CreateMediaScreen(navController)
        }
    }
}
