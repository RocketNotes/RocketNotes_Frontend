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

@Composable
fun TeachersScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showAddTeacherForm by remember { mutableStateOf(false) }
    var showEditTeacherDialog by remember { mutableStateOf<Pair<Int, TeacherResource>?>(null) }
    val teachers = remember { mutableStateListOf<TeacherResource>() }
    var filteredTeachers by remember { mutableStateOf(teachers.toList()) } // Nueva lista para profesores filtrados

    // Obtener el token desde SharedPreferences
    val context = LocalContext.current
    val token = getToken(context)

    // Llamar a la API para obtener la lista de profesores al iniciar
    LaunchedEffect(Unit) {
        val call = RetrofitClient.placeHolder.getTeachers("Bearer $token")
        call.enqueue(object : Callback<List<TeacherResource>> {
            override fun onResponse(call: Call<List<TeacherResource>>, response: Response<List<TeacherResource>>) {
                if (response.isSuccessful) {
                    response.body()?.let { teacherList ->
                        teachers.clear()
                        teachers.addAll(teacherList)
                        filteredTeachers = teachers.toList() // Actualiza la lista filtrada con todos los profesores inicialmente
                    }
                } else {
                    // Manejar el error de respuesta
                }
            }

            override fun onFailure(call: Call<List<TeacherResource>>, t: Throwable) {
                // Manejar el error de red
            }
        })
    }

    // Filtrar la lista cada vez que el texto de búsqueda cambie
    LaunchedEffect(searchText) {
        filteredTeachers = if (searchText.isEmpty()) {
            teachers.toList() // Mostrar todos los profesores si el campo de búsqueda está vacío
        } else {
            teachers.filter { teacher ->
                teacher.firstName.contains(searchText, ignoreCase = true) ||
                        teacher.paternalLastName.contains(searchText, ignoreCase = true) ||
                        teacher.maternalLastName.contains(searchText, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = { TopNavBar(navController) },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTeacherForm = true },
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

            // Lista de profesores filtrados
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredTeachers.forEachIndexed { index, teacher ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${teacher.firstName} ${teacher.paternalLastName} ${teacher.maternalLastName}", fontSize = 20.sp)
                        IconButton(onClick = { showEditTeacherDialog = Pair(index, teacher) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Profesor")
                        }
                    }
                }
            }

            // Mostrar formulario para agregar profesor
            if (showAddTeacherForm) {
                AddTeacherForm(
                    onDismiss = { showAddTeacherForm = false },
                    onAddTeacher = { newTeacher ->
                        val call = RetrofitClient.placeHolder.addTeacher(newTeacher, "Bearer $token")
                        call.enqueue(object : Callback<TeacherResource> {
                            override fun onResponse(call: Call<TeacherResource>, response: Response<TeacherResource>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { addedTeacher ->
                                        teachers.add(addedTeacher)
                                        filteredTeachers = teachers.toList() // Actualiza la lista filtrada
                                    }
                                }
                            }

                            override fun onFailure(call: Call<TeacherResource>, t: Throwable) {
                                // Manejar error
                            }
                        })
                        showAddTeacherForm = false
                    }
                )
            }

            // Mostrar diálogo para editar profesor
            showEditTeacherDialog?.let { (index, teacher) ->
                EditTeacherDialog(
                    initialTeacher = teacher,
                    onDismiss = { showEditTeacherDialog = null },
                    onSaveTeacher = { updatedTeacher ->
                        val call = RetrofitClient.placeHolder.updateTeacher(teacher.id, updatedTeacher, "Bearer $token")
                        call.enqueue(object : Callback<TeacherResource> {
                            override fun onResponse(call: Call<TeacherResource>, response: Response<TeacherResource>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { savedTeacher ->
                                        teachers[index] = savedTeacher
                                        filteredTeachers = teachers.toList()
                                    }
                                }
                                showEditTeacherDialog = null
                            }
                            override fun onFailure(call: Call<TeacherResource>, t: Throwable) { showEditTeacherDialog = null }
                        })
                    },
                    onDeleteTeacher = {
                        val call = RetrofitClient.placeHolder.deleteTeacher(teacher.id, "Bearer $token")
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    teachers.removeAt(index)
                                    filteredTeachers = teachers.toList()
                                }
                                showEditTeacherDialog = null
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) { showEditTeacherDialog = null }
                        })
                    }
                )
            }
        }
    }
}

@Composable
fun AddTeacherForm(onDismiss: () -> Unit, onAddTeacher: (TeacherResource) -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var paternalLastName by remember { mutableStateOf("") }
    var maternalLastName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Profesor") },
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
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newTeacher = TeacherResource(
                    null, firstName, paternalLastName, maternalLastName, dni, phone, email
                )
                onAddTeacher(newTeacher)
            }) {
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
fun EditTeacherDialog(
    initialTeacher: TeacherResource,
    onDismiss: () -> Unit,
    onSaveTeacher: (TeacherResource) -> Unit,
    onDeleteTeacher: () -> Unit
) {
    var firstName by remember { mutableStateOf(initialTeacher.firstName) }
    var paternalLastName by remember { mutableStateOf(initialTeacher.paternalLastName) }
    var maternalLastName by remember { mutableStateOf(initialTeacher.maternalLastName) }
    var dni by remember { mutableStateOf(initialTeacher.dni) }
    var phone by remember { mutableStateOf(initialTeacher.phone) }
    var email by remember { mutableStateOf(initialTeacher.email) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Profesor") },
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
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedTeacher = TeacherResource(
                    initialTeacher.id,
                    firstName,
                    paternalLastName,
                    maternalLastName,
                    dni,
                    phone,
                    email
                )
                onSaveTeacher(updatedTeacher)
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
            OutlinedButton(onClick = onDeleteTeacher, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) {
                Text("Eliminar")
            }
        }
    )
}