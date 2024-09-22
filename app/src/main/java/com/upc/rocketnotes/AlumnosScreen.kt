package com.upc.rocketnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AlumnosScreen(navController: NavHostController){
    var searchText by remember { mutableStateOf("") }
    var showAddStudentForm by remember { mutableStateOf(false) }
    val students = remember { mutableListOf("Juan Perez", "Ana Gomez", "Carlos Rodriguez") }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController)},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddStudentForm = true },
                containerColor = Color.Blue
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Agregar Alumno")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barra de búsqueda y botón de agregar alumno
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Buscar alumno") }
                )
            }

            // Lista de alumnos
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                students.forEach { student ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = student, fontSize = 20.sp)
                        IconButton(onClick = { /* Acción de editar alumno */ }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Alumno")
                        }
                    }
                }
            }

            // Formulario de agregar alumno
            if (showAddStudentForm) {
                AddStudentForm(
                    onAddStudent = { newStudent ->
                        students.add(newStudent)
                        showAddStudentForm = false
                    },
                    onCancel = { showAddStudentForm = false }
                )
            }
        }
    }
}


@Composable
fun AddStudentForm(onAddStudent: (String) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var assignedTeacher by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") }
        )

        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Apellido") }
        )

        TextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = birthdate,
            onValueChange = { birthdate = it },
            label = { Text("Fecha de nacimiento (YYYY-MM-DD)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = assignedTeacher,
            onValueChange = { assignedTeacher = it },
            label = { Text("Profesor asignado") }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                val newStudent = "$name $surname"
                onAddStudent(newStudent)
            }) {
                Text("Agregar Alumno")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}