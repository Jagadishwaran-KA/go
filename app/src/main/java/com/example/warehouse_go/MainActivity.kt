package com.example.warehouse_go

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warehouse_go.ui.theme.Warehouse_GoTheme
import receiveCards


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            Warehouse_GoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home"){
                    composable("home") {
                        Home(navController)
                    }
                    composable("Receive"){
                        ReceiveScreen(navController, receiveCards = receiveCards)
                    }
                }
            }
        }
    }
}

