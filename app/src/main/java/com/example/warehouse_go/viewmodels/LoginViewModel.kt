package com.example.warehouse_go.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warehouse_go.models.AuthCredentials
import com.example.warehouse_go.models.AuthState
import com.example.warehouse_go.services.MicrosoftOAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    private val activity: Activity
) : ViewModel() {

    private val tag = "LoginViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(tenantId: String, clientId: String, companyUrl: String) {
        viewModelScope.launch {
            try {
                if (tenantId.isBlank() || clientId.isBlank() || companyUrl.isBlank()) {
                    _authState.value = AuthState.Error("All fields are required")
                    return@launch
                }

                Log.d(tag, "Starting login with tenantId: $tenantId, clientId: $clientId")
                _authState.value = AuthState.Loading

                    val user = MicrosoftOAuthService(context, activity,AuthCredentials.OAuthCredentials(tenantId,clientId,companyUrl))
                    if (user.login()){
                        _authState.value = AuthState.Success(user.currentAccount?.username ?: "")
                    }else{
                        _authState.value = AuthState.Error("ERROR LOGGING IN")
                    }
            } catch (e: Exception) {
                Log.e(tag, "Login exception: ${e.message}")
                e.printStackTrace()
                _authState.value = AuthState.Error("An error occurred: ${e.message}")
            }
        }
    }


    }

