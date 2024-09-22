package com.upc.rocketnotes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationHost()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("success") { SuccessScreen(navController) }
        composable("home"){ HomeScreen(navController) }
        composable("profile"){ ProfileScreen(navController) }
        composable("notifications"){ NotificationsScreen(navController) }
        composable("messages"){ MessagesScreen(navController) }
    }
}
