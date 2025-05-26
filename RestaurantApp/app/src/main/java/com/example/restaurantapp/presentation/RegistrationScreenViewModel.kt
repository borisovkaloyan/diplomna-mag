package com.example.restaurantapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.util.sha256
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ApiApi
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ServerError
import org.openapitools.client.infrastructure.Success
import org.openapitools.client.models.UserRegistration
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiApi
) : ViewModel() {

    fun registerUser(
        username: String,
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val hashedPassword = password.sha256()
                val user = UserRegistration(username, hashedPassword, firstName, lastName, email)

                val response = withContext(Dispatchers.IO) {
                    api.apiUsersRegisterCreateWithHttpInfo(user)
                }
                when (response) {
                    is Success -> onSuccess(response.data?.message ?: "Success")
                    is ClientError -> onError(response.body?.toString() ?: "Client Error")
                    is ServerError -> onError(response.body?.toString() ?: "Server Error")
                    else -> onError("Unexpected error")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message}")
            }
        }
    }
}
