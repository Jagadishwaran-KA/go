package com.example.warehouse_go.models

import java.util.Date

data class User(
    val userName: String,
    val accessToken: String,
    val expiresOn: Date
)
