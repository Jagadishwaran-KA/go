package com.example.warehouse_go.services

import com.example.warehouse_go.models.AuthCredentials
import com.example.warehouse_go.models.User

interface Auth {
    suspend fun Login(): Boolean
    suspend fun Logout(): Boolean

    fun isLoggedIn() : Boolean
    fun getUserInfo(): User

}