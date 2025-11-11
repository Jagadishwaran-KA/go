package com.example.warehouse_go

import Receive
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveScreen(navController: NavController, receiveCards: List<Receive>) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receive", fontSize = 24.sp) },
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
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF00695C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Warehouse Receipt", fontSize = 20.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(28.dp)
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            // LazyColumn for swipable cards
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(receiveCards, key = { it.receiptNo }) { card ->
                    SwipableReceiveCard(receiveCard = card)
                }
            }
        }
    }
}

@Composable
fun TwoColumnLabelValueRow(
    firstLabel: String,
    firstValue: String,
    secondLabel: String,
    secondValue: String,
    labelFontSize: TextUnit = 20.sp,
    valueFontSize: TextUnit = 18.sp
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = firstLabel, fontSize = labelFontSize, fontWeight = FontWeight.Medium)
            Text(text = firstValue, fontSize = valueFontSize, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = secondLabel, fontSize = labelFontSize, fontWeight = FontWeight.Medium)
            Text(text = secondValue, fontSize = valueFontSize, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ReceiveCard(
    receiveCard: Receive,
    modifier: Modifier = Modifier,
    cardColor: Color = Color(0xFFF3F1F1),
    cornerRadius: Dp = 10.dp,
    contentPadding: Dp = 12.dp,
    rowSpacing: Dp = 12.dp
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Column(modifier = Modifier.padding(contentPadding)) {
            TwoColumnLabelValueRow(
                firstLabel = "Receipt No",
                firstValue = receiveCard.receiptNo,
                secondLabel = "Assigned Date",
                secondValue = receiveCard.assignedDate
            )
            Spacer(Modifier.height(rowSpacing))
            TwoColumnLabelValueRow(
                firstLabel = "Purchase Order",
                firstValue = receiveCard.orderNo,
                secondLabel = "Assigned Time",
                secondValue = receiveCard.assignedTime
            )
        }
    }
}

@Composable
fun SwipableReceiveCard(receiveCard: Receive) {
    val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }
    val scope = rememberCoroutineScope()

    ReceiveCard(
        receiveCard = receiveCard,
        modifier = Modifier
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Animate back to original position
                        scope.launch {
                            offsetX.animateTo(0f, animationSpec = androidx.compose.animation.core.tween(300))
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        val newOffset = offsetX.value + dragAmount
                        scope.launch { offsetX.snapTo(newOffset) }
                    }
                )
            }
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    )
}
