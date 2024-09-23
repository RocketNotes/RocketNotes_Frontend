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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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

@Composable
fun AlumnosScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showAddStudentForm by remember { mutableStateOf(false) }
    var showEditStudentDialog by remember { mutableStateOf<Pair<Int, StudentResource>?>(null) }
    val students = remember { mutableStateListOf<StudentResource>() }
    val classrooms = remember { listOf("Aula 101", "Aula 102", "Aula 103") }
/////////////////////////
    val context = LocalContext.current
    val token = getToken(context) // Obtener el token
//////////////////////////
    // Obtener la lista de alumnos al cargar la pantalla
    LaunchedEffect(Unit) {
        val call = RetrofitClient.placeHolder.getStudents("Bearer $token")
        call.enqueue(object : Callback<List<StudentResource>> {
            override fun onResponse(call: Call<List<StudentResource>>, response: Response<List<StudentResource>>) {
                if (response.isSuccessful) {
                    response.body()?.let { studentList ->
                        students.addAll(studentList) // Agregar la lista de alumnos
                    }
                } else {
                    // Manejar error
                }
            }

            override fun onFailure(call: Call<List<StudentResource>>, t: Throwable) {
                // Manejar error de red
            }
        })
    }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddStudentForm = true },
                containerColor = Color.Green
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Agregar Alumno", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(text = "Lista de Alumnos", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(10.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                students.forEachIndexed { index, student ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Cambiar esta línea para mostrar el nombre completo
                        Text(text = "${student.firstName} ${student.paternalLastName} ${student.maternalLastName}", fontSize = 20.sp)
                        IconButton(onClick = { showEditStudentDialog = Pair(index, student) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Alumno")
                        }
                    }
                }
            }

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

            showEditStudentDialog?.let { (index, student) ->
                EditStudentDialog(
                    initialName = student.firstName,
                    initialPaternalLastName = student.paternalLastName, // Añade el apellido paterno
                    initialMaternalLastName = student.maternalLastName, // Añade el apellido materno
                    onDismiss = { showEditStudentDialog = null },
                    onSaveStudent = { updatedStudent ->
                        students[index] = updatedStudent
                        showEditStudentDialog = null
                    },
                    onDeleteStudent = {
                        students.removeAt(index)
                        showEditStudentDialog = null
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
                    "", // Pasa los apellidos adecuados si los tienes
                    "", // Pasa los apellidos adecuados si los tienes
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
        }
    )
}