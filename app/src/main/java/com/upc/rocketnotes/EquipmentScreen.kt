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
fun EquipmentScreen(navController: NavHostController) {
    var showAddEquipmentForm by remember { mutableStateOf(false) }
    var equipmentList = remember { mutableStateListOf<SchoolEquipmentResource>() }
    var showEditEquipmentDialog by remember { mutableStateOf<Pair<Int, SchoolEquipmentResource>?>(null) }

    Scaffold(
        topBar = { TopNavBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEquipmentForm = true },
                containerColor = Color.Green
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Equipment", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(text = "School Equipment List", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(10.dp))

            // Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Category", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Quantity", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Text(text = "Status", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(40.dp)) // Space for edit icon
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            // Equipment Data
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                equipmentList.forEachIndexed { index, equipment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Data in table format
                        Text(text = equipment.name, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = equipment.category, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = "${equipment.quantity}", fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Text(text = equipment.status, fontSize = 14.sp, modifier = Modifier.weight(1f))

                        // Edit icon for status
                        IconButton(onClick = { showEditEquipmentDialog = Pair(index, equipment) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Status")
                        }
                    }
                }
            }
        }

        // Form to add new equipment
        if (showAddEquipmentForm) {
            AddSchoolEquipmentForm(
                onDismiss = { showAddEquipmentForm = false },
                onAddEquipment = { newEquipment ->
                    equipmentList.add(newEquipment)
                    showAddEquipmentForm = false
                }
            )
        }

        // Dialog to edit equipment status
        showEditEquipmentDialog?.let { (index, equipment) ->
            EditSchoolEquipmentDialog(
                initialName = equipment.name,
                initialCategory = equipment.category,
                initialQuantity = equipment.quantity,
                initialStatus = equipment.status,
                onDismiss = { showEditEquipmentDialog = null },
                onSaveEquipment = { updatedEquipment ->
                    equipmentList[index] = updatedEquipment
                    showEditEquipmentDialog = null
                }
            )
        }
    }
}

@Composable
fun AddSchoolEquipmentForm(onDismiss: () -> Unit, onAddEquipment: (SchoolEquipmentResource) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Equipment") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Equipment Name") }
                )
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") }
                )
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("Status") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newEquipment = SchoolEquipmentResource(
                        name = name,
                        category = category,
                        quantity = quantity.toIntOrNull() ?: 0,
                        status = status
                    )
                    onAddEquipment(newEquipment)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditSchoolEquipmentDialog(
    initialName: String,
    initialCategory: String,
    initialQuantity: Int,
    initialStatus: String,
    onDismiss: () -> Unit,
    onSaveEquipment: (SchoolEquipmentResource) -> Unit
) {
    var updatedName by remember { mutableStateOf(initialName) }
    var updatedCategory by remember { mutableStateOf(initialCategory) }
    var updatedQuantity by remember { mutableStateOf(initialQuantity.toString()) }
    var updatedStatus by remember { mutableStateOf(initialStatus) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Equipment") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Equipment Name") }
                )
                TextField(
                    value = updatedCategory,
                    onValueChange = { updatedCategory = it },
                    label = { Text("Category") }
                )
                TextField(
                    value = updatedQuantity,
                    onValueChange = { updatedQuantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = updatedStatus,
                    onValueChange = { updatedStatus = it },
                    label = { Text("Status") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedEquipment = SchoolEquipmentResource(
                    name = updatedName,
                    category = updatedCategory,
                    quantity = updatedQuantity.toIntOrNull() ?: 0,
                    status = updatedStatus
                )
                onSaveEquipment(updatedEquipment)
            }) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Example of the School Equipment data class
data class SchoolEquipmentResource(
    val name: String,
    val category: String,
    val quantity: Int,
    val status: String
)