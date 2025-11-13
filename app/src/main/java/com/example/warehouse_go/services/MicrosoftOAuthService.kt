package com.example.warehouse_go.services

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.warehouse_go.models.AuthCredentials
import com.microsoft.identity.client.AcquireTokenParameters
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.PublicClientApplicationConfiguration
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.Date

class MicrosoftOAuthService(private val context: Context,private val activity: Activity,authCredentials: AuthCredentials) : Auth {

    private var oauthCredentials: AuthCredentials.OAuthCredentials = authCredentials as? AuthCredentials.OAuthCredentials ?: throw IllegalArgumentException("Expected OAuthCredentials")

    private val tag = "MicrosoftAuthService"

    private val scopes = listOf("https://api.businesscentral.dynamics.com/.default")

    private val redirectUri : String = "msalwarehousego://auth"

    private val authorityUrl: String = "https://login.microsoftonline.com/${oauthCredentials.tenantId}"

    private var singleAccountApp: IPublicClientApplication ? = null

    var currentAccount: IAccount? = null
        private set

    var accessToken: String = ""
        private set
    private var expiresOn: Date = Date()

    override suspend fun login(): Boolean {

        return withContext(Dispatchers.Main){
            try {
                val appCreated = CompletableDeferred<Boolean>()
                val authResult = CompletableDeferred<Boolean>()

                val listener = object : IPublicClientApplication.ApplicationCreatedListener {
                    override fun onCreated(application: IPublicClientApplication?) {
                        Log.d(tag, "MSAL SingleAccount app initialized successfully")
                        singleAccountApp = application
                        appCreated.complete(true)
                    }
                    override fun onError(exception: MsalException) {
                        Log.e(tag, "MSAL initialization failed: ${exception.message}")
                        appCreated.complete(false)
                    }
                }

                PublicClientApplication.create(context,oauthCredentials.clientId,authorityUrl,redirectUri,listener)
                
                if (!appCreated.await()) {
                    return@withContext false
                }

                val parameters = AcquireTokenParameters.Builder()
                    .startAuthorizationFromActivity(activity)
                    .withScopes(scopes)
                    .withCallback(getAuthInteractiveCallback(authResult)).build()

                Log.d(tag, "Calling acquireToken()...")
                singleAccountApp?.acquireToken(parameters)

                return@withContext authResult.await()

            }catch (e: Exception){
                Log.e(tag, "Exception during interactive sign-in: ${e.message}")
                e.printStackTrace()
                return@withContext false
            }

        }
    }

    override fun logout(): Boolean {
        accessToken = ""
        expiresOn = Date()
        currentAccount = null
        singleAccountApp = null
        return true
    }

    override fun isLoggedIn(): Boolean {
        return currentAccount != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun acquireTokenSilent(): Boolean {

        if (currentAccount == null) {
            Log.w(tag, "Cannot acquire token silently: no account found")
            return false
        }

        if (!isTokenExpired()) {
            Log.d(tag, "Token is still valid for account=${currentAccount?.username}")
            return true
        }

        val authResult = CompletableDeferred<Boolean>()

        val silentParameters = AcquireTokenSilentParameters.Builder()
            .withScopes(scopes)
            .forAccount(currentAccount)
            .fromAuthority(currentAccount?.authority)
            .withCallback(getAuthInteractiveCallback(authResult))
            .build()

        singleAccountApp?.acquireTokenSilentAsync(silentParameters)
            ?: run {
                Log.e(tag, "MSAL app not initialized, cannot acquire token silently")
                return false
            }

        return try {
            val result = authResult.await()
            if (result) {
                Log.d(tag, "AcquireTokenSilent succeeded for account=${currentAccount?.username}")
            } else {
                Log.e(tag, "AcquireTokenSilent failed for account=${currentAccount?.username}")
            }
            result
        } catch (e: Exception) {
            Log.e(tag, "Exception during acquireTokenSilent: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    private fun getAuthInteractiveCallback(authResult: CompletableDeferred<Boolean>) : AuthenticationCallback{

        return object :AuthenticationCallback{
            override fun onCancel() {
                Log.d(tag, "Authentication canceled by user")
                authResult.complete(false)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                Log.d(tag, "Authentication success")

                authenticationResult?.let {
                    currentAccount = authenticationResult.account
                    accessToken = authenticationResult.accessToken
                    expiresOn = authenticationResult.expiresOn
                }
                authResult.complete(true)
            }

            override fun onError(exception: MsalException?) {
                Log.e(tag, "Authentication error: ${exception?.message}")
                exception?.printStackTrace()
                authResult.complete(false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isTokenExpired(): Boolean {
        return Instant.now().isAfter(expiresOn.toInstant())
    }

}
