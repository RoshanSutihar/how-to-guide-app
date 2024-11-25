package com.example.invasivespecies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.lawrence.androidgrades.GradesModel
@Composable
fun GuideScreen(vm: GradesModel, modifier: Modifier = Modifier) {
    var guideName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }  // Error state
    var successMessage by remember { mutableStateOf<String?>(null) }  // Success message state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(16.dp, 50.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Add Guide", style = MaterialTheme.typography.headlineMedium)

        // Input field for guide name
        OutlinedTextField(
            value = guideName,
            onValueChange = { guideName = it },
            label = { Text("Guide Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Display error message if there is any
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display success message if the guide was added successfully
        successMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                if (guideName.isNotBlank()) {
                    // Call ViewModel's addGuide function
                    vm.addGuide(guideName) { success, message ->
                        if (success) {
                            successMessage = message
                            guideName = ""  // Clear the input field
                        } else {
                            errorMessage = message
                        }
                    }
                } else {
                    errorMessage = "Please enter a guide name."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Guide")
        }
    }
}
