package com.upc.rocketnotes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    var showEditFacilityDialog by remember { mutableStateOf<Pair<Int, FacilityResource>?>(null) }
    val facilities = remember { mutableStateListOf<FacilityResource>() }
    var filteredFacilities by remember { mutableStateOf(facilities.toList()) }

    // Obtener el token desde SharedPreferences
    val context = LocalContext.current
    val token = getToken(context)

    // Llamar a la API para obtener la lista de instalaciones
    LaunchedEffect(Unit) {
        val call = RetrofitClient.placeHolder.getFacilities("Bearer $token")
        call.enqueue(object : Callback<List<FacilityResource>> {
            override fun onResponse(call: Call<List<FacilityResource>>, response: Response<List<FacilityResource>>) {
                if (response.isSuccessful) {
                    response.body()?.let { facilityList ->
                        facilities.clear()
                        facilities.addAll(facilityList)
                        filteredFacilities = facilities.toList()
                    }
                }
            }

            override fun onFailure(call: Call<List<FacilityResource>>, t: Throwable) {
                // Manejar el error de red
            }
        })
    }

    // Filtrar instalaciones según el texto de búsqueda
    LaunchedEffect(searchText) {
        filteredFacilities = if (searchText.isEmpty()) {
            facilities.toList()
        } else {
            facilities.filter { it.name.contains(searchText, ignoreCase = true) }
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
                Icon(Icons.Filled.AddCircle, contentDescription = "Agregar Instalación", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(text = "Lista de Instalaciones", fontSize = 28.sp)
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
                    label = { Text("Buscar Instalación") }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                        Text(text = facility.name, fontSize = 20.sp)
                        IconButton(onClick = { showEditFacilityDialog = Pair(index, facility) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Instalación")
                        }
                    }
                }
            }

            if (showAddFacilityForm) {
                AddFacilityForm(
                    onDismiss = { showAddFacilityForm = false },
                    onAddFacility = { newFacility ->
                        val call = RetrofitClient.placeHolder.addFacility(newFacility, "Bearer $token")
                        call.enqueue(object : Callback<FacilityResource> {
                            override fun onResponse(call: Call<FacilityResource>, response: Response<FacilityResource>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { addedFacility ->
                                        facilities.add(addedFacility)
                                        filteredFacilities = facilities.toList()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<FacilityResource>, t: Throwable) {
                                // Manejar el error de red
                            }
                        })
                        showAddFacilityForm = false
                    }
                )
            }

            showEditFacilityDialog?.let { (index, facility) ->
                EditFacilityDialog(
                    initialFacility = facility,
                    onDismiss = { showEditFacilityDialog = null },
                    onSaveFacility = { updatedFacility ->
                        facilities[index] = updatedFacility
                        filteredFacilities = facilities.toList()
                        showEditFacilityDialog = null
                    }
                )
            }
        }
    }
}

@Composable
fun AddFacilityForm(onDismiss: () -> Unit, onAddFacility: (FacilityResource) -> Unit) {
    var name by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var creation by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Instalación") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                TextField(value = period, onValueChange = { period = it }, label = { Text("Periodo") })
                TextField(value = budget, onValueChange = { budget = it }, label = { Text("Presupuesto") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                TextField(value = creation, onValueChange = { creation = it }, label = { Text("Creación") })
                TextField(value = status, onValueChange = { status = it }, label = { Text("Estado") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(onClick = {
                val newFacility = FacilityResource(null, name, period, creation, budget.toIntOrNull() ?: 0, status.toIntOrNull() ?: 0)
                onAddFacility(newFacility)
            }) { Text("Agregar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun EditFacilityDialog(
    initialFacility: FacilityResource,
    onDismiss: () -> Unit,
    onSaveFacility: (FacilityResource) -> Unit
) {
    var name by remember { mutableStateOf(initialFacility.name) }
    var period by remember { mutableStateOf(initialFacility.period) }
    var creation by remember { mutableStateOf(initialFacility.creation) }
    var budget by remember { mutableStateOf(initialFacility.budget.toString()) }
    var status by remember { mutableStateOf(initialFacility.status.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Instalación") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                TextField(value = period, onValueChange = { period = it }, label = { Text("Periodo") })
                TextField(value = budget, onValueChange = { budget = it }, label = { Text("Presupuesto") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                TextField(value = creation, onValueChange = { creation = it }, label = { Text("Creación") })
                TextField(value = status, onValueChange = { status = it }, label = { Text("Estado") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedFacility = FacilityResource(
                    initialFacility.id, name, period, creation, budget.toIntOrNull() ?: 0, status.toIntOrNull() ?: 0
                )
                onSaveFacility(updatedFacility)
            }) { Text("Guardar Cambios") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

