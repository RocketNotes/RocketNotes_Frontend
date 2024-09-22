package com.upc.rocketnotes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Icon

@Composable
fun BottomNavBar(navController: NavHostController){
    NavigationBar(
        modifier = Modifier.shadow(8.dp),
        containerColor = Color(0xFFFFFFFF)
    ) {
        val grayColor = Color(0xFF888888) // Definir un color gris medio
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile Icon", tint = grayColor) },
            label = { Text("Profile") },
            selected = navController.currentDestination?.route == "profile",
            onClick = { navController.navigate("profile") },
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home Icon", tint = grayColor) },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") },
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notifications Icon", tint = grayColor) },
            label = { Text("Notifications") },
            selected = navController.currentDestination?.route == "notifications",
            onClick = { navController.navigate("notifications") },
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Email, contentDescription = "Messages Icon", tint = grayColor) },
            label = { Text("Messages") },
            selected = navController.currentDestination?.route == "messages",
            onClick = { navController.navigate("messages") },
        )
    }
}