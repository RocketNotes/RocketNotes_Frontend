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
    var showAddClassroomForm by remember { mutableStateOf(false) }
    var showEditClassroomDialog by remember { mutableStateOf<Pair<Int, Classroom>?>(null) } // Almacena el índice y el profesor a editar
    val classrooms = remember {
        mutableListOf(
            Classroom(
                "123",
                "1ero de primaria",
                "A",
                "30",

            ),
            Classroom(
                "342",
                "3ero de primaria",
                "B",
                "35",

            ),
            Classroom(
                "332",
                "3ero de primaria",
                "A",
                "30",

            )
        )
    }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddClassroomForm = true },
                containerColor = Color.Green
            ) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Agregar Clases",
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
            Text(text = "Lista de Aulas", fontSize = 28.sp)
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
                    label = { Text("Buscar Aula") }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Lista de Aulas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                classrooms.forEachIndexed { index, classroom ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${classroom.getName()}", fontSize = 20.sp)
                        IconButton(onClick = { showEditClassroomDialog = Pair(index, classroom) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Aula")
                        }
                    }
                }
            }

            // Formulario de agregar Aula
            if (showAddClassroomForm) {
                AddClassroomForm(
                    onDismiss = { showAddClassroomForm = false },
                    onAddClassroom = { newClassroom ->
                        classrooms.add(newClassroom)
                        showAddClassroomForm = false
                    }
                )
            }

            // Mostrar el diálogo de editar profesor
            showEditClassroomDialog?.let { (index, classroom) ->
                EditClassroomDialog(
                    initialId = classroom.getId(),
                    initialName = classroom.getName(),
                    initialSection = classroom.getSection(),
                    initialCapacity = classroom.getCapacity(),
                    onDismiss = { showEditClassroomDialog = null },
                    onSaveClassroom = { updatedId, updatedName, updatedSection, updatedCapacity ->
                        // Actualiza los datos del profesor
                        val updatedClassroom = classroom.copy(
                            updatedId, updatedName, updatedSection, updatedCapacity
                        )
                        classrooms[index] = updatedClassroom
                        showEditClassroomDialog = null
                    },
                    onDeleteClassroom = {
                        classrooms.removeAt(index)
                        showEditClassroomDialog = null
                    }
                )
            }
        }
    }
}

@Composable
fun AddClassroomForm(onDismiss: () -> Unit, onAddClassroom: (Classroom) -> Unit) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Aula") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID") }
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = section,
                    onValueChange = { section = it },
                    label = { Text("Seccion") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Capacidad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newClassroom = Classroom(
                        id,
                        name,
                        section,
                        capacity
                    )
                    onAddClassroom(newClassroom)
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
    initialId: String,
    initialName: String,
    initialSection: String,
    initialCapacity: String,
    onDismiss: () -> Unit,
    onSaveClassroom: (String, String, String, String) -> Unit,
    onDeleteClassroom: () -> Unit
) {
    // Variables de estado para cada campo
    var updatedId by remember { mutableStateOf(initialId) }
    var updatedName by remember { mutableStateOf(initialName) }
    var updatedSection by remember { mutableStateOf(initialSection) }
    var updatedCapacity by remember { mutableStateOf(initialCapacity) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Aula") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Campo de ID
                TextField(
                    value = updatedId,
                    onValueChange = { updatedId = it },
                    label = { Text("ID") }
                )

                // Campo de nombre
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Nombre") }
                )


                // Campo de Seccion
                TextField(
                    value = updatedSection,
                    onValueChange = { updatedSection = it },
                    label = { Text("Seccion") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                // Campo de fecha de Capacidad
                TextField(
                    value = updatedCapacity,
                    onValueChange = { updatedCapacity = it },
                    label = { Text("Capacity") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Botón de eliminar profesor
                OutlinedButton(
                    onClick = onDeleteClassroom,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Eliminar Aula")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSaveClassroom(
                    updatedId,
                    updatedName,
                    updatedSection,
                    updatedCapacity

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
