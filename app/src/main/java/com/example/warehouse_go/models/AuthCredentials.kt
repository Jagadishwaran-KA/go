package com.example.warehouse_go.models

sealed class AuthCredentials {
    abstract val companyUrl : String

    data class OAuthCredentials(val tenantId: String, val clientId: String,override val companyUrl: String): AuthCredentials()

    data class BasicAuthCredentials(val userName: String, val password: String, override val companyUrl: String): AuthCredentials()

}