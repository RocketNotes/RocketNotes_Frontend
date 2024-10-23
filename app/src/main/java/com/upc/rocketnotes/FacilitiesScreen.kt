package com.upc.rocketnotes

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
    var showAddFacilityForm by remember { mutableStateOf(false) }
    var facilities = remember { mutableStateListOf<FacilityResource>() }
    var showEditFacilityDialog by remember { mutableStateOf<Pair<Int, FacilityResource>?>(null) }

    Scaffold(
        topBar = { TopNavBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddFacilityForm = true },
                containerColor = Color.Green
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Agregar Facility", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(text = "Lista de Facilities", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // Encabezado de la tabla
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Period", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Budget", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Status", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(40.dp)) // Espacio para el ícono de editar
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            // Datos de los facilities
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                facilities.forEachIndexed { index, facility ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Datos de la tabla
                        Text(text = facility.name, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = facility.period, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = "$${facility.budget}", fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = facility.status, fontSize = 14.sp, modifier = Modifier.weight(1f))

                        // Icono para editar el estado
                        IconButton(onClick = { showEditFacilityDialog = Pair(index, facility) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Estado")
                        }
                    }
                }
            }
        }

        // Formulario para añadir un nuevo facility
        if (showAddFacilityForm) {
            AddFacilityForm(
                onDismiss = { showAddFacilityForm = false },
                onAddFacility = { newFacility ->
                    facilities.add(newFacility)
                    showAddFacilityForm = false
                }
            )
        }

        // Diálogo para editar el estado del facility
        showEditFacilityDialog?.let { (index, facility) ->
            EditFacilityDialog(
                initialName = facility.name,
                initialStatus = facility.status,
                initialBudget = facility.budget,
                initialPeriod = facility.period,
                onDismiss = { showEditFacilityDialog = null },
                onSaveFacility = { updatedFacility ->
                    facilities[index] = updatedFacility
                    showEditFacilityDialog = null
                }
            )
        }
    }
}

@Composable
fun AddFacilityForm(onDismiss: () -> Unit, onAddFacility: (FacilityResource) -> Unit) {
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }

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
                    label = { Text("Nombre del Facility") }
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
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("Estado del Facility") }
                )


            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newFacility = FacilityResource(
                        name = name,
                        status = status,
                        budget = budget.toDoubleOrNull() ?: 0.0,
                        period = period
                    )
                    onAddFacility(newFacility)
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
fun EditFacilityDialog(
    initialName: String,
    initialStatus: String,
    initialBudget: Double,
    initialPeriod: String,
    onDismiss: () -> Unit,
    onSaveFacility: (FacilityResource) -> Unit
) {
    var updatedName by remember { mutableStateOf(initialName) }
    var updatedStatus by remember { mutableStateOf(initialStatus) }
    var updatedBudget by remember { mutableStateOf(initialBudget.toString()) }
    var updatedPeriod by remember { mutableStateOf(initialPeriod) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Facility") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Nombre del Facility") }
                )
                TextField(
                    value = updatedPeriod,
                    onValueChange = { updatedPeriod = it },
                    label = { Text("Período") }
                )
                TextField(
                    value = updatedBudget,
                    onValueChange = { updatedBudget = it },
                    label = { Text("Presupuesto") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = updatedStatus,
                    onValueChange = { updatedStatus = it },
                    label = { Text("Estado del Facility") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedFacility = FacilityResource(
                    name = updatedName,
                    status = updatedStatus,
                    budget = updatedBudget.toDoubleOrNull() ?: 0.0,
                    period = updatedPeriod
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
        }
    )
}

// Ejemplo de la clase de datos para Facility
data class FacilityResource(
    val name: String,
    val status: String,
    val budget: Double,
    val period: String
)