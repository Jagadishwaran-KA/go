package com.example.warehouse_go

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.warehouse_go.models.AuthState
import com.example.warehouse_go.viewmodels.LoginViewModel

@Composable
fun Login(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as Activity
    
    val viewModel: LoginViewModel = remember {
        LoginViewModel(context, activity)
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
                viewModel.resetAuthState()
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
            ConnectionSettingsSection(navController, viewModel, authState)
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
fun ConnectionSettingsSection(navController: NavHostController, viewModel: LoginViewModel, authState: AuthState) {
    var tenantId by remember { mutableStateOf(TextFieldValue("")) }
    var clientId by remember { mutableStateOf(TextFieldValue("")) }
    var companyUrl by remember { mutableStateOf(TextFieldValue("")) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp)
            .padding(14.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        SettingsHeader()
        
        LoginField(
            label = "Tenant Id", 
            suffixIcon = Icons.Default.ContentCopy,
            value = tenantId,
            onValueChange = { tenantId = it },
            enabled = authState !is AuthState.Loading
        )
        LoginField(
            label = "Client Id", 
            suffixIcon = Icons.Default.ContentCopy,
            value = clientId,
            onValueChange = { clientId = it },
            enabled = authState !is AuthState.Loading
        )
        LoginField(
            label = "Company Url", 
            suffixIcon = Icons.Default.ContentCopy,
            value = companyUrl,
            onValueChange = { companyUrl = it },
            enabled = authState !is AuthState.Loading
        )
        
        ActionButtons(
            navController = navController,
            isLoading = authState is AuthState.Loading,
            onConnectClick = {
                viewModel.login(
                    tenantId = tenantId.text,
                    clientId = clientId.text,
                    companyUrl = companyUrl.text
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
fun LoginField(
    label: String, 
    suffixIcon: ImageVector,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.labelLarge) },
        placeholder = { Text(text = "Enter $label", style = MaterialTheme.typography.labelMedium) },
        trailingIcon = {
            Icon(imageVector = suffixIcon, contentDescription = label)
        },
        singleLine = true,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    )
}

@Composable
fun SpacerWithBox(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(height))
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        content()
    }
}


@Composable
fun ActionButtons(navController: NavHostController, isLoading: Boolean, onConnectClick: () -> Unit) {
    SpacerWithBox(modifier = Modifier) {
        FilledButton(
            Modifier.width(350.dp),
            label = if (isLoading) "Connecting..." else "Connect to Warehouse",
            enabled = !isLoading
        ) {
            onConnectClick()
        }
    }
    SpacerWithBox(modifier = Modifier) {
        HorizontalDivider(Modifier.width(300.dp).padding(6.dp))
    }
    SpacerWithBox(modifier = Modifier) {
        OutLineButton(
            Modifier.width(350.dp),
            label = "Scan QR Code to Login",
            icon = Icons.Default.QrCode,
            enabled = !isLoading
        )
    }
    SpacerWithBox(modifier = Modifier) {
        HelpText()
    }
}


@Composable
fun FilledButton(modifier: Modifier = Modifier, label: String, enabled: Boolean = true, click: () -> Unit) {
    Button(
        onClick = {click()},
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OutLineButton(modifier: Modifier = Modifier, label: String, icon: ImageVector? = null, enabled: Boolean = true) {
    OutlinedButton(
        onClick = {},
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(4.dp)
    ) {
        icon?.let {
            it -> Icon(imageVector = it, contentDescription = label)
        }
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
    )
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
