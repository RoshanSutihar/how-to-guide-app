package edu.lawrence.androidgrades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideDetailScreen(
    vm: GradesModel,
    guideId: Int,
    guideName: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(guideId) {
        vm.getCommentsForGuide(guideId)
    }

    val commentsList by vm.narrationList.collectAsState()

    var heading by remember { mutableStateOf("") }
    var narration by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = guideName,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        // Column that holds the content inside the Scaffold
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .imePadding() // Ensures the content is adjusted when the keyboard appears
        ) {
            // LazyColumn for displaying the comments
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Ensures it takes up available space
                    .padding(bottom = 8.dp) // Prevents clipping at the bottom
            ) {
                items(commentsList) { comment ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = comment.heading,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = comment.narration,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        HorizontalDivider(thickness = 2.dp)
                    }
                }
            }

            // Column for the input fields and Add Comment button
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp) // Padding for the bottom part of the form
            ) {
                // Heading TextField
                TextField(
                    value = heading,
                    onValueChange = { heading = it },
                    label = { Text("Heading") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Narration TextField
                TextField(
                    value = narration,
                    onValueChange = { narration = it },
                    label = { Text("Narration") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Add Comment Button
                Button(
                    onClick = {
                        if (heading.isNotBlank() && narration.isNotBlank()) {
                            vm.addCommentToGuide(guideId, heading, narration)
                            heading = ""
                            narration = ""
                            focusManager.clearFocus() // Dismiss the keyboard
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Comment")
                }
            }
        }
    }
}
