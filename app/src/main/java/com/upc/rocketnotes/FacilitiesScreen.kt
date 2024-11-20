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
fun FacilitiesScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var showAddFacilityForm by remember { mutableStateOf(false) }
    var showEditFacilityDialog by remember { mutableStateOf<Pair<Int, Facility>?>(null) }
    val facilities = remember { mutableStateListOf<Facility>() }
    var filteredFacilities by remember { mutableStateOf(facilities.toList()) }

    // Obtener el token desde SharedPreferences
    val context = LocalContext.current
    val token = getToken(context)

    // Llamar a la API para obtener la lista de facilities al iniciar
    LaunchedEffect(Unit) {
        val call = RetrofitClient.placeHolder.getAllFacilities("Bearer $token")
        call.enqueue(object : Callback<List<Facility>> {
            override fun onResponse(call: Call<List<Facility>>, response: Response<List<Facility>>) {
                if (response.isSuccessful) {
                    response.body()?.let { facilityList ->
                        facilities.clear()
                        facilities.addAll(facilityList)
                        filteredFacilities = facilities.toList()
                    }
                }
            }

            override fun onFailure(call: Call<List<Facility>>, t: Throwable) {
                // Manejar el error de red
            }
        })
    }

    // Filtrar la lista cada vez que el texto de búsqueda cambie
    LaunchedEffect(searchText) {
        filteredFacilities = if (searchText.isEmpty()) {
            facilities.toList()
        } else {
            facilities.filter { facility ->
                facility.name.contains(searchText, ignoreCase = true) ||
                        facility.period.contains(searchText, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddFacilityForm = true },
                containerColor = Color.Green
            ) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Agregar Facility",
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
            Text(text = "Facilities Management", fontSize = 28.sp)
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
                    label = { Text("Buscar facility") }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Lista de facilities filtradas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredFacilities.forEachIndexed { index, facility ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${facility.name} - ${facility.period}", fontSize = 20.sp)
                        IconButton(onClick = { showEditFacilityDialog = Pair(index, facility) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Facility")
                        }
                    }
                }
            }

            // Mostrar formulario para agregar facility
            if (showAddFacilityForm) {
                AddFacilityForm(
                    onDismiss = { showAddFacilityForm = false },
                    onAddFacility = { newFacility ->
                        val call = RetrofitClient.placeHolder.createFacility(newFacility, "Bearer $token")
                        call.enqueue(object : Callback<Facility> {
                            override fun onResponse(call: Call<Facility>, response: Response<Facility>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { addedFacility ->
                                        facilities.add(addedFacility)
                                        filteredFacilities = facilities.toList()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Facility>, t: Throwable) {
                                // Manejar error
                            }
                        })
                        showAddFacilityForm = false
                    }
                )
            }

            // Mostrar diálogo para editar facility
            showEditFacilityDialog?.let { (index, facility) ->
                EditFacilityDialog(
                    initialFacility = facility,
                    onDismiss = { showEditFacilityDialog = null },
                    onSaveFacility = { updatedFacility ->
                        val call = RetrofitClient.placeHolder.updateFacility(facility.id, updatedFacility, "Bearer $token")
                        call.enqueue(object : Callback<Facility> {
                            override fun onResponse(call: Call<Facility>, response: Response<Facility>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { savedFacility ->
                                        facilities[index] = savedFacility
                                        filteredFacilities = facilities.toList()
                                    }
                                }
                                showEditFacilityDialog = null
                            }
                            override fun onFailure(call: Call<Facility>, t: Throwable) { showEditFacilityDialog = null }
                        })
                    },
                    onDeleteFacility = {
                        val call = RetrofitClient.placeHolder.deleteFacility(facility.id, "Bearer $token")
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    facilities.removeAt(index)
                                    filteredFacilities = facilities.toList()
                                }
                                showEditFacilityDialog = null
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) { showEditFacilityDialog = null }
                        })
                    }
                )
            }
        }
    }
}
@Composable
fun AddFacilityForm(onDismiss: () -> Unit, onAddFacility: (Facility) -> Unit) {
    var name by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var creation by remember { mutableStateOf("") } // Nuevo campo para creación

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Facility") },
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
                    value = period,
                    onValueChange = { period = it },
                    label = { Text("Período") }
                )
                TextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Presupuesto") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = creation,
                    onValueChange = { creation = it },
                    label = { Text("Creación (YYYY-MM-DD HH:mm:ss)") } // Formato sugerido
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newFacility = Facility(
                    null,
                    period,
                    budget.toIntOrNull() ?: 0,
                    creation,
                    name,
                )
                onAddFacility(newFacility)
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
fun EditFacilityDialog(
    initialFacility: Facility,
    onDismiss: () -> Unit,
    onSaveFacility: (Facility) -> Unit,
    onDeleteFacility: () -> Unit
) {
    var name by remember { mutableStateOf(initialFacility.name) }
    var period by remember { mutableStateOf(initialFacility.period) }
    var budget by remember { mutableStateOf(initialFacility.budget.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Facility") },
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
                    value = period,
                    onValueChange = { period = it },
                    label = { Text("Período") }
                )
                TextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Presupuesto") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedFacility = Facility(
                    initialFacility.id, // Conservar el ID existente
                    period,
                    budget.toIntOrNull(),
                    initialFacility.creation, // No cambiar el campo de creación
                    name
                )
                onSaveFacility(updatedFacility)
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
            OutlinedButton(
                onClick = onDeleteFacility,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Eliminar")
            }
        }
    )
}


fun fetchFacilities(token: String, onResult: (List<Facility>) -> Unit) {
    val call = RetrofitClient.placeHolder.getAllFacilities("Bearer $token")
    call.enqueue(object : Callback<List<Facility>> {
        override fun onResponse(call: Call<List<Facility>>, response: Response<List<Facility>>) {
            if (response.isSuccessful) {
                val facilities = response.body() ?: emptyList()
                onResult(facilities)
            } else {
                println("Error al obtener facilities: ${response.errorBody()?.string()}")
                onResult(emptyList()) // Notificar lista vacía en caso de error
            }
        }

        override fun onFailure(call: Call<List<Facility>>, t: Throwable) {
            println("Error de red: ${t.message}")
            onResult(emptyList()) // Notificar lista vacía en caso de fallo
        }
    })
}

fun deleteFacility(facilityId: Long, token: String, onDeleted: () -> Unit, onError: () -> Unit) {
    val call = RetrofitClient.placeHolder.deleteFacility(facilityId, "Bearer $token")
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                println("Facility eliminada con éxito")
                onDeleted() // Llamar a la función de éxito
            } else {
                println("Error al eliminar facility: ${response.errorBody()?.string()}")
                onError() // Llamar a la función de error
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            println("Error de red al eliminar facility: ${t.message}")
            onError() // Llamar a la función de error
        }
    })
}

