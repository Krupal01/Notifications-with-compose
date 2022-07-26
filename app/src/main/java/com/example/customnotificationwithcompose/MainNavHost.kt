package com.example.customnotificationwithcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MainScreen.route ){
        composable(
            route = Screens.MainScreen.route
        ){
            MainScreen()
        }

        composable(
            route = Screens.NotificationScreen.route,
            deepLinks = listOf(
                navDeepLink{uriPattern = "app://notification.com/notification"}
            )
        ){
            NotificationScreen()
        }
    }
}

sealed class Screens(val route : String){
    object MainScreen : Screens(route = "main screen")
    object NotificationScreen : Screens(route = "notification screen")
}