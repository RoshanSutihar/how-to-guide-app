package edu.lawrence.androidgrades

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role.Companion.DropdownList
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.invasivespecies.GuideScreen
import edu.lawrence.androidgrades.ui.theme.AndroidGradesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(GradesModel::class.java)

        setContent {
            AndroidGradesTheme {
                GradesApp(vm)
            }
        }
    }
}

@Composable
fun GradesApp(vm: GradesModel) {
    val navController = rememberNavController()
    val items = listOf("Guides", "Add Guide")
    val icons = listOf(Icons.Filled.Menu, Icons.Filled.Add)


    Scaffold(
        bottomBar = {
            if (navController.currentDestination?.route != "guideDetail/{guideId}/{guideName}") {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = navController.currentDestination?.route == item,
                            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            onClick = {
                                if (item == "Guides") {
                                    if (navController.currentDestination?.route != "Guides") {
                                        navController.navigate("Guides") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    } else {
                                        navController.popBackStack(route = "Guides", inclusive = false)
                                    }
                                } else {
                                    navController.navigate(item) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }

                // bottom nav end
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Correct padding applied here
        ) {
            NavHost(
                navController = navController,
                startDestination = "Guides"
            ) {
                composable(route = "Guides") {
                    vm.loadGuides()
                    GuideListScreen(vm, navController, Modifier.fillMaxSize())
                }
                composable(route = "guideDetail/{guideId}/{guideName}") { backStackEntry ->
                    val guideId = backStackEntry.arguments?.getString("guideId")?.toInt() ?: 0
                    val guideName = backStackEntry.arguments?.getString("guideName") ?: "Unknown Guide"
                    GuideDetailScreen(vm, guideId, guideName, navController)
                }
                composable(route = "Add Guide") { navBackStackEntry ->
                    GuideScreen(
                        vm = vm,
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }

}




