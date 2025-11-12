package com.example.warehouse_go

import com.example.warehouse_go.models.Receive
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveScreen(navController: NavController, receiveCards: List<Receive>) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receive", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Warehouse Receipt", style = MaterialTheme.typography.titleSmall) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(22.dp)
                    )
                },
                maxLines = 1,
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
                items(receiveCards, key = { it.receiptNo }) { card ->
                    Receipt(card)
                }
            }
        }
    }
}

@Composable
fun Receipt(receiptInfo: Receive,modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxWidth().padding(2.dp)
    ) {
        Column(modifier.padding(2.dp)){
            BuildRow("Receipt No", receiptInfo.receiptNo)
            BuildRow("Purch Order No",receiptInfo.orderNo)
            BuildRow("Assigned Date",receiptInfo.assignedDate)
            BuildRow("Assigned Time",receiptInfo.assignedTime)
        }
    }
}

@Composable
fun BuildRow(label: String, value: String,modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween){
        Text(label, color = MaterialTheme.colorScheme.primary,style = MaterialTheme.typography.titleMedium)
        Text(value, fontWeight = FontWeight.SemiBold,style = MaterialTheme.typography.titleMedium)
    }
}


