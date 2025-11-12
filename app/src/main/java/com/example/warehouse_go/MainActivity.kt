package com.example.warehouse_go

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warehouse_go.models.receiveCards
import com.example.warehouse_go.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home"){
                    composable("login"){
                        LoginScreen(navController)
                    }
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("receiveInfo"){
                        ReceiveInfoScreen(navController, receiveCards = receiveCards)
                    }
                    composable("receiptDetail"){
                        ReceiptDetail()
                    }
                }
            }
        }
    }
}

