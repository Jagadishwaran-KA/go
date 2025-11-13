package com.example.warehouse_go.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.warehouse_go.models.AuthCredentials
import com.example.warehouse_go.models.User
import com.microsoft.identity.client.AcquireTokenParameters
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MicrosoftOAuthService(private val context: Context,private val activity: Activity,authCredentials: AuthCredentials) : Auth {

    private var oauthCredentials: AuthCredentials.OAuthCredentials = authCredentials as?
                AuthCredentials.OAuthCredentials ?: throw IllegalArgumentException("Expected OAuthCredentials")

    private val tag = "MicrosoftAuthService"

    private var currentAccount: IAccount? = null

    private val SCOPES = listOf("https://api.businesscentral.dynamics.com/.default")

    private val redirectUri : String = "msalwarehousego://auth"

    private val authorityUrl: String = "https://login.microsoftonline.com/${oauthCredentials.tenantId}"

    private var singleAccountApp: IPublicClientApplication? = null

    override suspend fun Login(): Boolean {

        return withContext(Dispatchers.Main){
            try {
                val appCreated = CompletableDeferred<Boolean>()
                Log.d(tag,"INSIDE LOGIN")
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
                
                Log.d(tag, "Waiting for MSAL app initialization...")
                if (!appCreated.await()) {
                    Log.e(tag, "Failed to create MSAL application")
                    return@withContext false
                }
                
                Log.d(tag, "MSAL app initialization completed successfully")
                
                val authResult = CompletableDeferred<Boolean>()
                
                Log.d(tag, "Building acquire token parameters...")
                val parameters = AcquireTokenParameters.Builder()
                    .startAuthorizationFromActivity(activity)
                    .withScopes(SCOPES)
                    .withCallback(getAuthInteractiveCallback()).build()

                Log.d(tag, "Calling acquireToken()...")
                singleAccountApp?.acquireToken(parameters)
                Log.d(tag, "acquireToken() called, waiting for callback...")

                return@withContext authResult.await()


            }catch (e: Exception){
                Log.e(tag, "Exception during interactive sign-in: ${e.message}")
                e.printStackTrace()
                return@withContext false
            }

        }

    }

    override suspend fun Logout(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(): User {
        return  User(
            displayName = "John Doe",
            userPrincipalName = "john.doe@example.com",
            id = "12345678-90ab-cdef-1234-567890abcdef",
            mail = "john.doe@example.com",
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            tenantId = "87654321-abcd-4321-efgh-1234567890ab",
            clientId = "abcdef12-3456-7890-abcd-ef1234567890"
        )
    }

    suspend fun acquireTokenSilent(){
        val silentParameters :  AcquireTokenSilentParameters = AcquireTokenSilentParameters.Builder()
            .withScopes(SCOPES)
            .forAccount(currentAccount)
            .fromAuthority(currentAccount?.authority)
            .withCallback(getAuthInteractiveCallback())
            .build();
        singleAccountApp?.acquireTokenSilentAsync(silentParameters);
    }

    private fun getAuthInteractiveCallback() : AuthenticationCallback{

        return object :AuthenticationCallback{
            override fun onCancel() {
                Log.d(tag, "Authentication canceled by user")
            }

            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                Log.d(tag, "Authentication success")

                authenticationResult?.let {
                    Log.d(tag,"Authentication Success - Username: ${authenticationResult.account.username}")
                    Log.d(tag,"Access Token: ${authenticationResult.accessToken}")
                }
            }

            override fun onError(exception: MsalException?) {
                Log.e(tag, "Authentication error: ${exception?.message}")
                Log.e(tag, "Error code: ${exception?.errorCode}")
                exception?.printStackTrace()
            }
        }
    }


}
