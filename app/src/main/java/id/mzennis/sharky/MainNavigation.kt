package id.mzennis.sharky

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.mzennis.sharky.home.HomeScreen
import id.mzennis.sharky.auth.SignInScreen
import id.mzennis.sharky.auth.SignUpScreen

object AppDestinations {
    const val SPLASH_ROUTE = "splash"
    const val SIGNIN_ROUTE = "signin"
    const val SIGNUP_ROUTE = "signup"
    const val HOME_ROUTE = "home"
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH_ROUTE,
        modifier = modifier
    ) {
        composable(AppDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(AppDestinations.SIGNIN_ROUTE) {
                        popUpTo(AppDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.SIGNIN_ROUTE) {
            val mainViewModel: MainViewModel = hiltViewModel()
            SignInScreen(
                onSignInClick = { _, _ ->
                    mainViewModel.onSignInSuccess()
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SIGNIN_ROUTE) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    mainViewModel.onSignInSuccess()
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SIGNIN_ROUTE) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(AppDestinations.SIGNUP_ROUTE)
                }
            )
        }

        composable(AppDestinations.SIGNUP_ROUTE) {
            val mainViewModel: MainViewModel = hiltViewModel()
            SignUpScreen(
                onSignUpClick = { _, _, _ ->
                    mainViewModel.onSignInSuccess()
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SIGNIN_ROUTE) { inclusive = true }
                    }
                },
                onSignUpSuccess = {
                    mainViewModel.onSignInSuccess()
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SIGNIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen()
        }
    }
}
