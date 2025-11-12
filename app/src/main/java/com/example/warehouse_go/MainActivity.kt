package com.example.warehouse_go

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warehouse_go.models.ReceiptDetails
import com.example.warehouse_go.ui.theme.AppTheme
import com.example.warehouse_go.models.receiveCards


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Login"){
                    composable("Login"){
                        Login(navController)
                    }
                    composable("home") {
                        Home(navController)
                    }
                    composable("Receive"){
                        ReceiveScreen(navController, receiveCards = receiveCards)
                    }
                    composable("ReceiveDetail"){
                        ReceiptDetail()
                    }
                }
            }
        }
    }
}

