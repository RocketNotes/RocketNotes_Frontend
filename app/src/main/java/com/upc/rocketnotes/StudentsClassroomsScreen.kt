package com.upc.rocketnotes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun StudentsClassroomsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val token = getToken(context) // Obtiene el token desde SharedPreferences

    var students by remember { mutableStateOf<List<StudentResource>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Llamada a la API para obtener estudiantes
    LaunchedEffect(Unit) {
        RetrofitClient.placeHolder.getStudents("Bearer $token").enqueue(object : Callback<List<StudentResource>> {
            override fun onResponse(call: Call<List<StudentResource>>, response: Response<List<StudentResource>>) {
                if (response.isSuccessful) {
                    response.body()?.let { fetchedStudents ->
                        students = fetchedStudents
                    }
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<StudentResource>>, t: Throwable) {
                errorMessage = "Failed to fetch students: ${t.message}"
            }
        })
    }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) } // Barra inferior
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Muestra error si ocurre
            errorMessage?.let {
                Text(
                    text = "Error: $it",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Muestra la lista de estudiantes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students) { student ->
                    StudentCard(student = student)
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: StudentResource) {
    // Estado para manejar el estado del interruptor
    var isAttendanceTaken by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = randomColor() // Color aleatorio para cada card
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Información del estudiante
            Text(
                text = "Nombre: ${student.firstName} ${student.paternalLastName} ${student.maternalLastName}",
                style = MaterialTheme.typography.bodyLarge
            )

            // Interruptor rectangular
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAttendanceTaken) "Asistencia tomada" else "Pendiente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isAttendanceTaken) Color(0xFF4CAF50) else Color(0xFFF44336) // Verde o rojo según el estado
                )

                Switch(
                    checked = isAttendanceTaken,
                    onCheckedChange = { isAttendanceTaken = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50), // Color del interruptor cuando está activado
                        uncheckedThumbColor = Color(0xFFF44336), // Color cuando está desactivado
                        checkedTrackColor = Color(0xFF81C784), // Color de la pista activada
                        uncheckedTrackColor = Color(0xFFEF9A9A) // Color de la pista desactivada
                    )
                )
            }
        }
    }
}
// Genera un color aleatorio
fun randomColor(): Color {
    val colors = listOf(
        Color(0xFFFFCDD2), // Light Red
        Color(0xFFBBDEFB), // Light Blue
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFD1C4E9)  // Light Purple
    )
    return colors.random()
}