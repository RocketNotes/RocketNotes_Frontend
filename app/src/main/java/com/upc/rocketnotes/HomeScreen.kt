package com.upc.rocketnotes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Icon
import kotlinx.coroutines.selects.select

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController){
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopNavBar()},
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ){
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hola {username}",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Botón Profesores
            ButtonWithIcon(
                text = "Profesores",
                icon = Icons.Default.Person,
                onClick = { navController.navigate("alumnos") }
            )

            // Botón Alumnos
            ButtonWithIcon(
                text = "Alumnos",
                icon = Icons.Default.Face,
                onClick = { navController.navigate("alumnos") }
            )

            // Botón Aulas
            ButtonWithIcon(
                text = "Aulas",
                icon = Icons.Default.Place,
                onClick = { navController.navigate("alumnos") }
            )

            // Botón Inventario
            ButtonWithIcon(
                text = "Inventario",
                icon = Icons.Default.Build,
                onClick = { navController.navigate("alumnos") }
            )

            // Botón Equipamiento escolar
            ButtonWithIcon(
                text = "Equipamiento Escolar",
                icon = Icons.Default.Info,
                onClick = { navController.navigate("alumnos") }
            )
        }
    }
}

@Composable
fun ButtonWithIcon(text: String, icon: ImageVector, onClick: () -> Unit) {
    val navController = rememberNavController()
    Button(
        onClick = onClick,
        modifier = Modifier.size(width = 350.dp, height = 80.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp), // Sombra
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Fondo blanco
            contentColor = Color.Black // Texto e íconos en negro
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.Black)
            Text(text, fontSize = 18.sp)
        }
    }
}
