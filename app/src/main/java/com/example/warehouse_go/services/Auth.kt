package com.example.warehouse_go.services

import com.example.warehouse_go.models.AuthCredentials
import com.example.warehouse_go.models.User

interface Auth {
    suspend fun login(): Boolean
    fun logout(): Boolean
    fun isLoggedIn() : Boolean

}