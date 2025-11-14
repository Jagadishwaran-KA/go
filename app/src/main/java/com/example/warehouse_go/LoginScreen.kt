package com.example.warehouse_go

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.warehouse_go.components.AppButtons
import com.example.warehouse_go.components.AppTextField
import com.example.warehouse_go.components.LayoutHelpers
import com.example.warehouse_go.models.AuthState
import com.example.warehouse_go.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as Activity
    
    val viewModel: LoginViewModel = remember {
        LoginViewModel(context,activity)
    }
    
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (authState as AuthState.Error).message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HeaderTitle()
            Text("Version 1.0.0", style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(horizontal = 14.dp))
            ConnectionSettingsSection(viewModel, authState)
        }
    }
}

@Composable
fun HeaderTitle() {
    Text(
        "Warehouse Go",
        Modifier
            .fillMaxWidth()
            .padding(30.dp),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ConnectionSettingsSection(viewModel: LoginViewModel, authState: AuthState) {
    var tenantId by remember { mutableStateOf(("")) }
    var clientId by remember { mutableStateOf(("")) }
    var companyUrl by remember { mutableStateOf(("")) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp)
            .padding(14.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        SettingsHeader()

        AppTextField.OutlinedTextField(
            label = "Tenant Id", 
            suffixIcon = Icons.Default.ContentCopy,
            value = tenantId,
            onValueChange = { tenantId = it },
            enabled = authState !is AuthState.Loading,
            placeholder = { Text("Enter Tenant Id",style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
        AppTextField.OutlinedTextField(
            label = "Client Id", 
            suffixIcon = Icons.Default.ContentCopy,
            value = clientId,
            onValueChange = { clientId = it },
            enabled = authState !is AuthState.Loading,
            placeholder = {Text("Enter Client Id", style = MaterialTheme.typography.labelMedium)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
        AppTextField.OutlinedTextField(
            label = "Company Url", 
            suffixIcon = Icons.Default.ContentCopy,
            value = companyUrl,
            onValueChange = { companyUrl = it },
            enabled = authState !is AuthState.Loading,
            placeholder = { Text("Enter Company Url", style = MaterialTheme.typography.labelMedium)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
        
        ActionButtons(
            isLoading = authState is AuthState.Loading,
            onConnectClick = {
                viewModel.login(
                    tenantId = tenantId,
                    clientId = clientId,
                    companyUrl = companyUrl
                )
            }
        )
    }
}

@Composable
fun SettingsHeader() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            "Connection Settings",
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Enter your Business Central Connection details",
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleSmall
        )
    }
}



@Composable
fun ActionButtons(isLoading: Boolean, onConnectClick: () -> Unit) {
    LayoutHelpers.CenteredSpacer{
        AppButtons.FilledButton(
            Modifier.width(350.dp),
            label = if (isLoading) "Connecting..." else "Connect to Warehouse",
            enabled = !isLoading
        ) {
            onConnectClick()
        }
    }
    LayoutHelpers.CenteredSpacer{ LayoutHelpers.HorizontalDivider(Modifier
        .width(300.dp)
        .padding(6.dp)) }
    LayoutHelpers.CenteredSpacer{
        AppButtons.OutlinedButton(
            Modifier.width(350.dp),
            label = "Scan QR Code to Login",
            icon = Icons.Default.QrCode,
            enabled = !isLoading
        ){
            //TODO Add QR Parsing Logic
        }
    }
    LayoutHelpers.CenteredSpacer { HelpText() }
}

@Composable
fun HelpText() {
    Text(
        "Need help with configuration? Contact your system administrator",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}
