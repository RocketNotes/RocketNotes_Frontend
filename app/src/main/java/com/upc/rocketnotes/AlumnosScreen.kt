package com.upc.rocketnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumnosScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showAddStudentForm by remember { mutableStateOf(false) }
    var showEditStudentDialog by remember { mutableStateOf<Pair<Int, StudentResource>?>(null) }
    val students = remember { mutableStateListOf<StudentResource>() }
    val classrooms = listOf("Aula 101", "Aula 102", "Aula 103")
    val context = LocalContext.current
    val token = getToken(context)

    // Obtener la lista de alumnos al cargar la pantalla
    LaunchedEffect(Unit) {
        val call = RetrofitClient.placeHolder.getStudents("Bearer $token")
        call.enqueue(object : Callback<List<StudentResource>> {
            override fun onResponse(call: Call<List<StudentResource>>, response: Response<List<StudentResource>>) {
                if (response.isSuccessful) {
                    response.body()?.let { studentList ->
                        students.addAll(studentList)
                    }
                }
            }

            override fun onFailure(call: Call<List<StudentResource>>, t: Throwable) {
                // Manejar error de red
            }
        })
    }

    Scaffold(
        topBar = { TopNavBar(navController) },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddStudentForm = true },
                containerColor = Color(0xFF1EC089),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Alumno")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(
                text = "Lista de Alumnos",
                fontSize = 24.sp,
                color = Color(0xFF1EC089),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Barra de búsqueda
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text("Buscar alumno") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color(0xFF1EC089)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de alumnos
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                students.forEachIndexed { index, student ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${student.firstName} ${student.paternalLastName} ${student.maternalLastName}",
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            IconButton(onClick = { showEditStudentDialog = Pair(index, student) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Alumno", tint = Color(0xFF1EC089))
                            }
                        }
                    }
                }
            }

            // Formulario para agregar estudiante
            if (showAddStudentForm) {
                AddStudentForm(
                    onDismiss = { showAddStudentForm = false },
                    onAddStudent = { newStudent ->
                        val call = RetrofitClient.placeHolder.addStudent(newStudent, "Bearer $token")
                        call.enqueue(object : Callback<StudentResource> {
                            override fun onResponse(call: Call<StudentResource>, response: Response<StudentResource>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { addedStudent ->
                                        students.add(addedStudent)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<StudentResource>, t: Throwable) {
                                // Manejar error
                            }
                        })
                        showAddStudentForm = false
                    },
                    classrooms = classrooms
                )
            }

            // Dialogo para editar estudiante
            showEditStudentDialog?.let { (index, student) ->
                EditStudentDialog(
                    initialName = student.firstName,
                    initialPaternalLastName = student.paternalLastName,
                    initialMaternalLastName = student.maternalLastName,
                    onDismiss = { showEditStudentDialog = null },
                    onSaveStudent = { updatedStudent ->
                        val call = RetrofitClient.placeHolder.updateStudent(student.id, updatedStudent, "Bearer $token")
                        call.enqueue(object : Callback<StudentResource> {
                            override fun onResponse(call: Call<StudentResource>, response: Response<StudentResource>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { savedStudent ->
                                        students[index] = savedStudent
                                    }
                                }
                                showEditStudentDialog = null
                            }

                            override fun onFailure(call: Call<StudentResource>, t: Throwable) {
                                showEditStudentDialog = null
                            }
                        })
                    },
                    onDeleteStudent = {
                        val call = RetrofitClient.placeHolder.deleteStudent(student.id, "Bearer $token")
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    students.removeAt(index)
                                }
                                showEditStudentDialog = null
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                showEditStudentDialog = null
                            }
                        })
                    },
                    classrooms = classrooms
                )
            }
        }
    }
}

@Composable
fun AddStudentForm(onDismiss: () -> Unit, onAddStudent: (StudentResource) -> Unit, classrooms: List<String>) {
    var firstName by remember { mutableStateOf("") }
    var paternalLastName by remember { mutableStateOf("") }
    var maternalLastName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var selectedClassroom by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Alumno") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = paternalLastName,
                    onValueChange = { paternalLastName = it },
                    label = { Text("Apellido Paterno") }
                )
                TextField(
                    value = maternalLastName,
                    onValueChange = { maternalLastName = it },
                    label = { Text("Apellido Materno") }
                )
                TextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Text(text = "Seleccionar Aula")
                TextField(
                    value = classrooms[selectedClassroom],
                    onValueChange = {},
                    label = { Text("Aula") },
                    readOnly = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val studentResource = StudentResource(
                        null,
                        firstName,
                        paternalLastName,
                        maternalLastName,
                        dni,
                        listOf(selectedClassroom.toInt()) // Convertir a lista de enteros
                    )
                    onAddStudent(studentResource)
                    onDismiss()
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
fun EditStudentDialog(
    initialName: String,
    initialPaternalLastName: String,
    initialMaternalLastName: String,
    onDismiss: () -> Unit,
    onSaveStudent: (StudentResource) -> Unit,
    onDeleteStudent: () -> Unit,
    classrooms: List<String>
) {
    var updatedName by remember { mutableStateOf(initialName) }
    var updatedPaternalLastName by remember { mutableStateOf(initialPaternalLastName) }
    var updatedMaternalLastName by remember { mutableStateOf(initialMaternalLastName) }
    var selectedClassroom by remember { mutableStateOf(classrooms[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Alumno") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = updatedPaternalLastName,
                    onValueChange = { updatedPaternalLastName = it },
                    label = { Text("Apellido Paterno") }
                )
                TextField(
                    value = updatedMaternalLastName,
                    onValueChange = { updatedMaternalLastName = it },
                    label = { Text("Apellido Materno") }
                )
                Text(text = "Seleccionar Aula")
                TextField(
                    value = selectedClassroom,
                    onValueChange = { selectedClassroom = it },
                    label = { Text("Aula") },
                    readOnly = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedStudent = StudentResource(
                    null, // Asigna el ID correcto si lo tienes
                    updatedName,
                    updatedPaternalLastName, // Pasa los apellidos adecuados si los tienes
                    updatedMaternalLastName, // Pasa los apellidos adecuados si los tienes
                    "", // Pasa el DNI adecuado si lo tienes
                    listOf(classrooms.indexOf(selectedClassroom)) // Convertir a lista de enteros
                )
                onSaveStudent(updatedStudent)
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
            OutlinedButton(onClick = onDeleteStudent, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) {
                Text("Eliminar")
            }
        }
    )
}