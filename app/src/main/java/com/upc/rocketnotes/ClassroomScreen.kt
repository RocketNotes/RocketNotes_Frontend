package com.upc.rocketnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
fun ClassroomScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showAddProfessorForm by remember { mutableStateOf(false) }
    var showEditTeacherDialog by remember { mutableStateOf<Pair<Int, Teacher>?>(null) } // Almacena el índice y el profesor a editar
    val teachers = remember {
        mutableListOf(
            Teacher(
                "Pedro",
                "Sanchez",
                "12345678",
                "1990-01-01",
                "Matemáticas"
            ),
            Teacher(
                "Ana",
                "Gonzalez",
                "87654321",
                "1985-05-15",
                "Ciencias"
            ),
            Teacher(
                "Carlos",
                "Hernandez",
                "11223344",
                "1980-03-20",
                "Historia"
            )
        )
    }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProfessorForm = true },
                containerColor = Color.Green
            ) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Agregar Profesor",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(text = "Lista de Profesores", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // Barra de búsqueda
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Buscar profesor") }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Lista de profesores
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                teachers.forEachIndexed { index, professor ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${professor.getName()} ${professor.getSurname()}", fontSize = 20.sp)
                        IconButton(onClick = { showEditTeacherDialog = Pair(index, professor) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Profesor")
                        }
                    }
                }
            }

            // Formulario de agregar profesor
            if (showAddProfessorForm) {
                AddTeacherForm(
                    onDismiss = { showAddProfessorForm = false },
                    onAddProfessor = { newProfessor ->
                        teachers.add(newProfessor)
                        showAddProfessorForm = false
                    }
                )
            }

            // Mostrar el diálogo de editar profesor
            showEditTeacherDialog?.let { (index, professor) ->
                EditTeacherDialog(
                    initialName = professor.getName(),
                    initialSurname = professor.getSurname(),
                    initialDni = professor.getDni(),
                    initialBirthdate = professor.getBirthdate(),
                    initialAssignedClass = professor.getAssignedClass(),
                    onDismiss = { showEditTeacherDialog = null },
                    onSaveProfessor = { updatedName, updatedSurname, updatedDni, updatedBirthdate, updatedAssignedClass ->
                        // Actualiza los datos del profesor
                        val updatedProfessor = professor.copy(
                            updatedName, updatedSurname, updatedDni, updatedBirthdate, updatedAssignedClass
                        )
                        teachers[index] = updatedProfessor
                        showEditTeacherDialog = null
                    },
                    onDeleteProfessor = {
                        teachers.removeAt(index)
                        showEditTeacherDialog = null
                    }
                )
            }
        }
    }
}

@Composable
fun AddClassroomForm(onDismiss: () -> Unit, onAddProfessor: (Teacher) -> Unit) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var assignedClass by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Profesor") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = { Text("Fecha de nacimiento (YYYY-MM-DD)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = assignedClass,
                    onValueChange = { assignedClass = it },
                    label = { Text("Clase asignada") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newTeacher = Teacher(
                        name,
                        surname,
                        dni,
                        birthdate,
                        assignedClass
                    )
                    onAddProfessor(newTeacher)
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditClassroomDialog(
    initialName: String,
    initialSurname: String,
    initialDni: String,
    initialBirthdate: String,
    initialAssignedClass: String,
    onDismiss: () -> Unit,
    onSaveProfessor: (String, String, String, String, String) -> Unit,
    onDeleteProfessor: () -> Unit
) {
    // Variables de estado para cada campo
    var updatedName by remember { mutableStateOf(initialName) }
    var updatedSurname by remember { mutableStateOf(initialSurname) }
    var updatedDni by remember { mutableStateOf(initialDni) }
    var updatedBirthdate by remember { mutableStateOf(initialBirthdate) }
    var updatedAssignedClass by remember { mutableStateOf(initialAssignedClass) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Profesor") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de nombre
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Nombre") }
                )

                // Campo de apellido
                TextField(
                    value = updatedSurname,
                    onValueChange = { updatedSurname = it },
                    label = { Text("Apellido") }
                )

                // Campo de DNI
                TextField(
                    value = updatedDni,
                    onValueChange = { updatedDni = it },
                    label = { Text("DNI") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                // Campo de fecha de nacimiento
                TextField(
                    value = updatedBirthdate,
                    onValueChange = { updatedBirthdate = it },
                    label = { Text("Fecha de nacimiento (YYYY-MM-DD)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                // Campo de clase asignada
                TextField(
                    value = updatedAssignedClass,
                    onValueChange = { updatedAssignedClass = it },
                    label = { Text("Clase Asignada") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de eliminar profesor
                OutlinedButton(
                    onClick = onDeleteProfessor,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Eliminar Profesor")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSaveProfessor(
                    updatedName,
                    updatedSurname,
                    updatedDni,
                    updatedBirthdate,
                    updatedAssignedClass
                )
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
