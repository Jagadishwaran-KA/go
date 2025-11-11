package com.example.warehouse_go

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Login(navController: NavHostController,modifier: Modifier = Modifier) {
    Scaffold{
           innerPadding ->
        Column(modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
                Text("Warehouse Go",Modifier
                    .fillMaxWidth()
                    .padding(28.dp), color = MaterialTheme.colorScheme.primary ,style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(14.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer)) {
                        Text("Connection Settings", modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth() , textAlign = TextAlign.Left,style = MaterialTheme.typography.titleLarge)
                        Text("Enter your Business Central Connection details",modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),textAlign = TextAlign.Left,style = MaterialTheme.typography.titleSmall)
                        LoginFormField(label = "Tenant Id",  suffixIcon = Icons.Default.ContentCopy)
                        LoginFormField(label = "Client Id",  suffixIcon = Icons.Default.ContentCopy)
                        LoginFormField(label = "Company Url",suffixIcon = Icons.Default.ContentCopy)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            FilledButton(modifier.fillMaxWidth())
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            HorizontalDivider(modifier.width(300.dp).padding(6.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            OutLineButton(modifier)
                        }
                }
        }
    }
}


@Composable
fun LoginFormField(
    modifier: Modifier = Modifier,
    label: String,
    suffixIcon: ImageVector,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = text,
        onValueChange = { newValue -> text = newValue },
        label = { Text(text = label, style = MaterialTheme.typography.labelLarge) },
        trailingIcon = {
            Icon(imageVector = suffixIcon, contentDescription = label)
        },
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    )
}

@Composable
fun FilledButton(modifier: Modifier = Modifier) {
    Button(onClick = {}, colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor =  MaterialTheme.colorScheme.onPrimary
    ),shape = RoundedCornerShape(12.dp), modifier = modifier.padding(4.dp)) {
        Text("Connect to Warehouse", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,modifier = modifier.padding(4.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun OutLineButton(modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
    ) {
        Text("Scan QR Code to Login", style = MaterialTheme.typography.titleMedium, modifier = modifier.padding(4.dp))
    }
}

