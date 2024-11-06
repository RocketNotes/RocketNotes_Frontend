package com.upc.rocketnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun NotificationsScreen(navController: NavHostController) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopNavBar() },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Notificación 1
            NotificationCard("El curso AH12 ha cambiado su sílabo")
            Spacer(modifier = Modifier.height(8.dp))

            // Notificación 2
            NotificationCard("El alumno U001 se retiró del curso JD31")
            Spacer(modifier = Modifier.height(8.dp))

            // Notificación 3
            NotificationCard("Se agregaron nuevos profesores")
        }
    }
}

@Composable
fun NotificationCard(notificationText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = notificationText,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            IconButton(onClick = { /* Acción para eliminar notificación */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon",
                    tint = Color.Red
                )
            }
        }
    }
}
