package com.example.warehouse_go.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.warehouse_go.models.User
import com.microsoft.identity.client.AcquireTokenParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication.ISingleAccountApplicationCreatedListener
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MicrosoftAuthService(
    private val context: Context,
    private val activity: Activity
) {
    private val tag = "MicrosoftAuthService"
    private var currentAccount: IAccount? = null
    private var _tenantId: String? = null
    private var _clientId: String? = null
    private var configFile: File? = null
    private val scopes = listOf("https://api.businesscentral.dynamics.com/.default")
    private var singleAccountApp: ISingleAccountPublicClientApplication? = null

    suspend fun signInInteractive(
        tenantId: String,
        clientId: String
    ): User? {
        return withContext(Dispatchers.Main) {
            try {
                Log.d(tag, "Starting interactive sign-in using JSON config...")

                val appCreated = CompletableDeferred<Boolean>()

                val listener = object : ISingleAccountApplicationCreatedListener {
                    override fun onCreated(application: ISingleAccountPublicClientApplication) {
                        Log.d(tag, "MSAL SingleAccount app initialized successfully")
                        singleAccountApp = application
                        appCreated.complete(true)
                    }

                    override fun onError(exception: MsalException) {
                        Log.e(tag, "MSAL initialization failed: ${exception.message}")
                        appCreated.complete(false)
                    }
                }

                // Always create new MSAL application and config file
                Log.d(tag, "Creating new MSAL application")

                singleAccountApp = null
                currentAccount = null
                _tenantId = tenantId
                _clientId = clientId

                configFile = createTempConfigFile(clientId, tenantId)
                Log.d(tag, "Created temp config file: ${configFile?.absolutePath}")

                PublicClientApplication.createSingleAccountPublicClientApplication(
                    context,
                    configFile!!,
                    listener
                )
                
                if (!appCreated.await()) {
                    Log.e(tag, "Failed to create MSAL application")
                    return@withContext null
                }

                val authResult = CompletableDeferred<User?>()

                val parameters = AcquireTokenParameters.Builder()
                    .startAuthorizationFromActivity(activity) 
                    .withScopes(scopes)
                    .withCallback(object : AuthenticationCallback {
                        override fun onSuccess(result: IAuthenticationResult?) {
                            Log.d(tag, "Authentication success")

                            result?.let {
                                currentAccount = it.account
                                val user = createUserFromAccount(
                                    it.account,
                                    it.accessToken,
                                    tenantId,
                                    clientId
                                )
                                
                                authResult.complete(user)
                            } ?: run {
                                Log.e(tag, "Authentication result is null")
                                authResult.complete(null)
                            }
                        }
                        override fun onError(exception: MsalException?) {
                            Log.e(tag, "Authentication error: ${exception?.message}")
                            exception?.printStackTrace()
                            authResult.complete(null)
                        }
                        override fun onCancel() {
                            Log.d(tag, "Authentication canceled by user")
                            authResult.complete(null)
                        }
                    })
                    .build()

                Log.d(tag, "Calling acquireToken()...")
                singleAccountApp?.acquireToken(parameters)

                return@withContext authResult.await()

            } catch (e: Exception) {
                Log.e(tag, "Exception during interactive sign-in: ${e.message}")
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    suspend fun logout(): Boolean {
        return withContext(Dispatchers.Main) {
            try {
                val deferred = CompletableDeferred<Boolean>()
                singleAccountApp?.signOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
                    override fun onSignOut() {
                        currentAccount = null
                        Log.d(tag, "Sign-out completed successfully")
                        deferred.complete(true)
                    }

                    override fun onError(exception: MsalException) {
                        Log.e(tag, "Sign-out failed: ${exception.message}")
                        deferred.complete(false)
                    }
                }) ?: run {
                    Log.d(tag, "No MSAL app to sign out from")
                    currentAccount = null
                    deferred.complete(true)
                }
                
                deferred.await()
            } catch (e: Exception) {
                Log.e(tag, "Exception during sign-out: ${e.message}")
                currentAccount = null
                false
            }
        }
    }

    private fun createTempConfigFile(clientId: String, tenantId: String): File {
        val configJson = createDynamicConfig(clientId, tenantId)

        val tempFile = File(context.cacheDir, "msal_config.json")
        tempFile.writeText(configJson)

        return tempFile
    }

    private fun createDynamicConfig(clientId: String, tenantId: String): String {
        val config = JSONObject().apply {
            put("client_id", clientId)
            put("account_mode", "SINGLE")
            put("authorization_user_agent", "WEBVIEW")
            put("redirect_uri", "https://api.businesscentral.dynamics.com")
            put("authorities", JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "AAD")
                    put("audience", JSONObject().apply {
                        put("type", "AzureADandPersonalMicrosoftAccount")
                    })
                    put("authority_url", "https://login.microsoftonline.com/$tenantId")
                })
            })
        }
        return config.toString()
    }

    private fun createUserFromAccount(
        account: IAccount,
        accessToken: String,
        tenantId: String,
        clientId: String
    ): User {
        return User(
            displayName = account.username,
            userPrincipalName = account.username,
            id = account.id,
            mail = account.username,
            accessToken = accessToken,
            tenantId = tenantId,
            clientId = clientId
        )
    }
}
