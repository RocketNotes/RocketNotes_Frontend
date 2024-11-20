package com.upc.rocketnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

@Composable
fun ClassroomListScreen(navController: NavHostController) {
    var classrooms by remember { mutableStateOf<List<ClassroomResource>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener el token desde SharedPreferences
    val context = LocalContext.current
    val token = getToken(context)

    // Verificamos que el token no sea nulo
    token?.let {
        // Llamada a la API para obtener las aulas con el token de autorización
        LaunchedEffect(Unit) {
            val call = RetrofitClient.placeHolder.getClassrooms("Bearer $it")
            call.enqueue(object : Callback<List<ClassroomResource>> {
                override fun onResponse(
                    call: Call<List<ClassroomResource>>,
                    response: Response<List<ClassroomResource>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { fetchedClassrooms ->
                            classrooms = fetchedClassrooms
                        }
                    } else {
                        errorMessage = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<List<ClassroomResource>>, t: Throwable) {
                    errorMessage = "Failed to fetch classrooms: ${t.message}"
                }
            })
        }
    } ?: run {
        // Si el token es nulo, mostrar un error o realizar alguna acción
        errorMessage = "Token no encontrado"
    }

    // Mostrar mensaje de error si ocurre alguno
    errorMessage?.let {
        Text(text = "Error: $it")
    }

    // Estructura con Scaffold
    Scaffold(
        topBar = { TopNavBar(navController) }, // Barra superior
        bottomBar = { BottomNavBar(navController = navController) } // Barra inferior
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            errorMessage?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(classrooms) { classroom ->
                    ClassroomCard(classroom, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ClassroomCard(classroom: ClassroomResource, navController: NavHostController) {
    val randomColor = generateRandomColor()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = randomColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Classroom: ${classroom.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Section: ${classroom.section}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Capacity: ${classroom.capacity}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("studentsClassrooms") // Navegación a la nueva vista
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tomar Asistencia")
            }
        }
    }
}

// Función para generar un color aleatorio
fun generateRandomColor(): Color {
    val red = Random.nextInt(50, 200)
    val green = Random.nextInt(50, 200)
    val blue = Random.nextInt(50, 200)
    return Color(red, green, blue)
}