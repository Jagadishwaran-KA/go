package com.example.warehouse_go

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.warehouse_go.components.LayoutHelpers
import com.example.warehouse_go.models.ReceiptDetails
import com.example.warehouse_go.models.ReceiptLine
import com.example.warehouse_go.models.receiptDetails

@Composable
fun ReceiptDetail(modifier: Modifier = Modifier) {
    val receiptDetails : ReceiptDetails = receiptDetails
    Column(modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)
        .padding(vertical = 50.dp, horizontal = 20.dp)) {
        ReceiptHeader(receiptDetails, modifier)
        Text("Receipt Lines", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
        LazyColumn {
            items(receiptDetails.receiptLines, key = {it.lineNo}){
                line -> BuildReceiptLine(line)
            }
        }
    }
}

@Composable
fun ReceiptHeader(receiptDetails: ReceiptDetails, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ) {
        Column(modifier = Modifier.padding(8.dp)) {
            LayoutHelpers.LabelValueRow(label = "Receipt: ", value = receiptDetails.receiptNo)
            LayoutHelpers.LabelValueRow(label = "Source Document: ", value = receiptDetails.sourceDocument)
            LayoutHelpers.LabelValueRow(label = "Source No: ", value = receiptDetails.sourceNo)
            LayoutHelpers.LabelValueRow(label = "Location Code: ", value = receiptDetails.locationCode)
            LayoutHelpers.LabelValueRow(label = "Zone Code: ", value = receiptDetails.zoneCode)
            LayoutHelpers.LabelValueRow(label = "Buy From Vendor: ", value = receiptDetails.vendorNo)
            LayoutHelpers.LabelValueRow(label = "Total Lines: ", value = receiptDetails.receiptLines.size.toString())
        }
    }
}

@Composable
fun BuildReceiptLine(line: ReceiptLine,modifier: Modifier = Modifier) {
    LayoutHelpers.AppCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            LayoutHelpers.LabelValueRow(label = "Line No: ", value = line.lineNo)
            LayoutHelpers.LabelValueRow(label = "Item No: ", value = line.itemNo)
            LayoutHelpers.LabelValueRow(label = "Item Description: ", value = line.description)
            LayoutHelpers.LabelValueRow(label = "Uom: ", value = line.uom)
            LayoutHelpers.LabelValueRow(label = "Bin Code: ", value = line.binCode)
            LayoutHelpers.LabelValueRow(label = "Due date: ", value = line.dueDate)
        }
    }
}