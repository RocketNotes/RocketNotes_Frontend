package com.upc.rocketnotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
//import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(navController: NavHostController) {
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Imagen de perfil
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)

                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop

                )
            }

            // Nombre, Email, Contraseña (Editable)
            EditableField(label = "Nombre", value = "Juan Smith")
            EditableField(label = "Email", value = "jsmith@gmail.com")
            EditableField(label = "Contraseña", value = "***************")

            // Tarjetas de resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(value = "120", label = "Aulas")
                InfoCard(value = "48", label = "Profesores")
                InfoCard(value = "570", label = "Alumnos")
            }
        }
    }
}

@Composable
fun EditableField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = value, fontSize = 18.sp)
            IconButton(onClick = { /* Acción de editar */ }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit $label")
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun InfoCard(value: String, label: String) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0xFFE0F7FA)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
