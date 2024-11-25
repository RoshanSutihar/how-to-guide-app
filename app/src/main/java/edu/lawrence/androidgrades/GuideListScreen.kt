package edu.lawrence.androidgrades

import android.view.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState


@Composable
fun GuideListScreen(
    vm: GradesModel,
    navController: NavController,
    modifier: Modifier
) {

    val guidesList = vm.guides.value ?: emptyList()

    Scaffold { innerPadding ->
        // Main column layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            LazyColumn(modifier = modifier) {
                items(guidesList) { guide ->
                    GuideItem(guide = guide) {

                        navController.navigate("guideDetail/${guide.guideId}/${guide.guideName}")
                    }
                }
            }
        }
    }
}

@Composable
fun GuideItem(guide: Guides, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = guide.guideName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}