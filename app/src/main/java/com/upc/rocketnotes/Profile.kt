package com.upc.rocketnotes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController){
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopNavBar()},
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ){
            innerPadding ->
        // Aquí puedes agregar el contenido de tu pantalla
        Text(
            text = "Profile",
            fontFamily = robotoFontFamily,
            modifier = Modifier.padding(innerPadding)
        )
    }
}