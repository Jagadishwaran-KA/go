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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import featureCards
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    val items = remember { mutableStateListOf(*featureCards) }
    val state = rememberReorderableLazyGridState(
        onMove = { from, to ->
            items.move(from.index, to.index)
        }
    )

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Warehouse Go", fontSize = 24.sp)
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                    )
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.NotificationsActive,
                        contentDescription = "Notification",
                    )
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                    )
                    Spacer(Modifier.width(10.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00695C),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) {

        innerPadding -> Column (modifier = Modifier.fillMaxSize().padding(innerPadding)){
            QuickScan(modifier = Modifier.fillMaxWidth().padding(8.dp))
            LazyVerticalGrid(
                state = state.gridState,
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state),
            ) {
                items(items, key = { it.name }) { feature ->
                    ReorderableItem(state, key = feature.name) { isDragging ->
                        DashBoard(
                            featureCard = feature,
                            modifier = Modifier
                                .then(if (isDragging) Modifier.shadow(8.dp) else Modifier)
                                .fillMaxWidth()
                                .padding(8.dp)
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

fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex == toIndex) return
    val item = removeAt(fromIndex)
    val adjustedIndex = if (toIndex > fromIndex) toIndex - 1 else toIndex
    add(adjustedIndex, item)
}