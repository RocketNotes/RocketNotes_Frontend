package com.upc.rocketnotes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.auth.User

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("success") { SuccessScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("profile/{userId}") { backStackEntry -> // Define el argumento userId
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                ProfileScreen(navController = navController, userId = userId.toLong())
            }
        }
        composable("notifications") { NotificationsScreen(navController) }
        composable("messages") { MessagesScreen(navController) }
        composable("alumnos") { AlumnosScreen(navController) }
        composable("profesores") { TeachersScreen(navController) }
        composable("facilities") { FacilitiesScreen(navController = navController)}
        composable("aulas") { ClassroomListScreen(navController = navController)}
        composable("studentsClassrooms") { StudentsClassroomsScreen(navController = navController)}
    }
}