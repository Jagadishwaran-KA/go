package com.example.warehouse_go.models

data class User(
    val displayName: String,
    val userPrincipalName: String,
    val id: String,
    val mail: String,
    val accessToken: String,
    val tenantId: String,
    val clientId: String
)
