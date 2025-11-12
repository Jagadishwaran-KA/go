package com.example.warehouse_go

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.warehouse_go.components.AppTextField
import com.example.warehouse_go.components.LayoutHelpers
import com.example.warehouse_go.models.FeatureCard
import com.example.warehouse_go.models.featureCards
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState


@Composable
fun HomeScreen(navController: NavHostController) {
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
            LayoutHelpers.AppTopBar(
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
                },)
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
                        DashBoardCard(
                            featureCard = feature,
                            modifier = Modifier
                                .longPressDraggableHandle()
                                .then(if (isDragging) Modifier.shadow(8.dp) else Modifier)
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    navController.navigate("receiveInfo")
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
    AppTextField.InputTextField(
        value = text,
        placeholder = "Scan Anything!",
        leadingIcon = Icons.Filled.QrCode2,
        onValueChange = { text = it },
        label = "Quick Scan",
        modifier = modifier
    )
}

@Composable
fun DashBoardCard(featureCard: FeatureCard,modifier: Modifier = Modifier) {
    LayoutHelpers.AppCard(
        modifier = modifier.size(160.dp),
        colors = CardDefaults.cardColors(containerColor = featureCard.color),
    ){
        Text(featureCard.count.toString(), textAlign = TextAlign.End, style = MaterialTheme.typography.titleLarge ,modifier = Modifier.fillMaxWidth().padding(8.dp), color = Color.White)
        Icon(
            imageVector = featureCard.icon,
            contentDescription = featureCard.name,
            modifier = Modifier
                .size(34.dp).align(Alignment.CenterHorizontally),
            tint = Color.White,
        )
        Spacer(Modifier.height(6.dp))
        Text(featureCard.name, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge,modifier = Modifier.fillMaxWidth().padding(8.dp),color = Color.White)
    }
}