package com.example.warehouse_go

import com.example.warehouse_go.models.Receive
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warehouse_go.components.AppTextField
import com.example.warehouse_go.components.LayoutHelpers


@Composable
fun ReceiveInfoScreen(navController: NavController, receiveCards: List<Receive>) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            LayoutHelpers.AppTopBar(
                title = { Text("Receive", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton({}) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AppTextField.OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Warehouse Receipt", style = MaterialTheme.typography.titleSmall) },
                leadingIcon = Icons.Filled.Search,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(receiveCards, key = { it.receiptNo }) { receiptInfo ->
                    SwipeableReceiptCard(receiptInfo) {
                       navController.navigate("receiptDetail")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableReceiptCard(
    receiptInfo: Receive,
    modifier: Modifier = Modifier,
    onSwipe: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onSwipe()
                    false
                }
                else -> false
            }
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            )
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        modifier = modifier
    ) {
        ReceiptInfoCard(receiptInfo)
    }
}

@Composable
fun ReceiptInfoCard(receiptInfo: Receive, modifier: Modifier = Modifier) {
    LayoutHelpers.AppCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(2.dp)) {
            LayoutHelpers.LabelValueRow("Receipt No", receiptInfo.receiptNo)
            LayoutHelpers.LabelValueRow("Purchase Order No", receiptInfo.orderNo)
            LayoutHelpers.LabelValueRow("Assigned Date", receiptInfo.assignedDate)
            LayoutHelpers.LabelValueRow("Assigned Time", receiptInfo.assignedTime)
        }
    }
}



