package com.example.warehouse_go

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.warehouse_go.models.featureCards
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    val items = remember { featureCards.toList().toMutableStateList() }
    val gridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(
        lazyGridState = gridState,
        onMove = { from, to ->
            items.add(to.index, items.removeAt(from.index))
        }
    )

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Warehouse Go", fontWeight = FontWeight.Medium ,style = MaterialTheme.typography.headlineMedium)
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Filled.NotificationsActive,
                        contentDescription = "Notification",
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                    )
                    Spacer(Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) {

        innerPadding -> Column (modifier = Modifier.fillMaxSize().padding(innerPadding)){
            QuickScan(modifier = Modifier.fillMaxWidth().padding(8.dp))
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items, key = { it.name }) { feature ->
                    ReorderableItem(reorderableLazyGridState, key = feature.name) { isDragging ->
                        DashBoard(
                            featureCard = feature,
                            modifier = Modifier
                                .longPressDraggableHandle()
                                .then(if (isDragging) Modifier.shadow(8.dp) else Modifier)
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    navController.navigate("Receive")
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickScan(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        placeholder = {
            Text("Scan Anything!")
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.QrCode2,
                contentDescription = "Barcode Scan"
            )
        },
        maxLines = 1,
        onValueChange = { text = it },
        label = { Text("Quick Scan") },
        modifier = modifier
    )
}