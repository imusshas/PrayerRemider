package com.rezau_mehedi.prayerreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rezau_mehedi.prayerreminder.presentation.Login
import com.rezau_mehedi.prayerreminder.presentation.PrayerTimeUI
import com.rezau_mehedi.prayerreminder.presentation.PreferenceViewModel
import com.rezau_mehedi.prayerreminder.presentation.SignUp
import com.rezau_mehedi.prayerreminder.presentation.VerifyOTP

@Composable
fun PrayerReminder(viewModel: PreferenceViewModel) {

    val navController = rememberNavController()

    val user = viewModel.user.collectAsState().value


    NavHost(
        navController = navController,
        startDestination = if (user.phoneNo.isEmpty()) {
            Routes.LOGIN.name
        } else {
            Routes.PRAYER_TIME_UI.name
        }
    ) {

        composable(route = Routes.LOGIN.name) {
            Login(
                viewModel = viewModel,
                navigateToHomeScreen = {
                    navigate(
                        navController,
                        Routes.LOGIN.name,
                        Routes.PRAYER_TIME_UI.name
                        )
                },
                navigateToSignUp = {
                    navigate(
                        navController,
                        Routes.LOGIN.name,
                        Routes.SIGN_UP.name
                    )
                }
            )
        }

        composable(
            route = Routes.SIGN_UP.name
        ) {
            SignUp(
                viewModel = viewModel,
                navigateVerifyOTP = {
                    navigate(
                        navController,
                        Routes.SIGN_UP.name,
                        Routes.VERIFY_OTP.name
                    )
                },
                navigateToLogin = {
                    navigate(
                        navController,
                        Routes.SIGN_UP.name,
                        Routes.LOGIN.name
                    )
                }
            )
        }

        composable(route = Routes.VERIFY_OTP.name) {
            VerifyOTP(
                viewModel = viewModel,
                navigateToPrayerTimeUI = {
                    navigate(
                        navController,
                        Routes.VERIFY_OTP.name,
                        Routes.PRAYER_TIME_UI.name
                        )
                }
            )
        }

        composable(route = Routes.PRAYER_TIME_UI.name) {
            PrayerTimeUI(
                viewModel = viewModel,
                navigateToSignUpScreen = {
                    navigate(
                        navController,
                        Routes.PRAYER_TIME_UI.name,
                        Routes.SIGN_UP.name
                    )
                }
            )
        }
    }


}


private fun navigate(
    navController: NavController,
    source: String,
    destination: String
) {
    navController.navigate(route = destination) {
        popUpTo(route = source) {
            inclusive = true
        }
        launchSingleTop = true
    }

}