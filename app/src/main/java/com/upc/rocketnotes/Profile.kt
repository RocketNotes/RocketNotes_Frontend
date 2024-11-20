package com.upc.rocketnotes

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavHostController, userId: Long) {
    val context = LocalContext.current
    val retrofit = RetrofitClient.retrofitInstance
    val apiService = retrofit.create(PlaceHolder::class.java)

    // Estados para almacenar los datos del usuario
    var username by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("N/A") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para seleccionar imagen de la galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // Obtener el token de SharedPreferences
    val token = getToken(context)

    // Hacer la solicitud para obtener los datos del usuario
    LaunchedEffect(Unit) {
        if (token != null) {
            apiService.getUserById(userId, "Bearer $token").enqueue(object : Callback<UserResource> {
                override fun onResponse(call: Call<UserResource>, response: Response<UserResource>) {
                    if (response.isSuccessful) {
                        response.body()?.let { userResource ->
                            username = userResource.username ?: "N/A"
                            role = userResource.role ?: "N/A"
                        }
                    } else {
                        errorMessage = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<UserResource>, t: Throwable) {
                    errorMessage = "Failed to fetch user data: ${t.message}"
                }
            })
        } else {
            errorMessage = "Token no encontrado"
        }
    }

    // Color principal
    val primaryColor = Color(0xFF1EC089)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) }
    ){
        // Mostrar la interfaz de usuario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(24.dp))
            // Imagen de perfil y botón de edición
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = if (imageUri != null) {
                        rememberAsyncImagePainter(imageUri)
                    } else {
                        painterResource(id = R.drawable.profile) // Imagen genérica
                    },
                    contentDescription = "Imagen de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .size(36.dp)
                    .background(primaryColor, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar imagen",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del usuario
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                color = primaryColor
            )
            Text(
                text = role,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // KPIs simulados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                KpiCard(label = "Aulas", value = "24")
                KpiCard(label = "Profesores", value = "12")
                KpiCard(label = "Alumnos", value = "350")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para cerrar sesión
            Button(
                onClick = {
                    // Acción para cerrar sesión
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text(text = "Cerrar Sesión", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun KpiCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .size(100.dp, 80.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
