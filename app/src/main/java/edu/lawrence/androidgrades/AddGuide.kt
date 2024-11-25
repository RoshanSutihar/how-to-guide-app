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
fun GuideScreen(vm: GradesModel, navController: NavController, modifier: Modifier = Modifier) {
    var guideName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(16.dp, 50.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Guide", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = guideName,
            onValueChange = { guideName = it },
            label = { Text("Guide Name") },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (guideName.isNotBlank()) {

                    vm.addGuide(guideName) { success, message, guideId ->
                        if (success) {
                            guideId?.let {
                                navController.navigate("guideDetail/$guideId/$guideName")
                            }
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
