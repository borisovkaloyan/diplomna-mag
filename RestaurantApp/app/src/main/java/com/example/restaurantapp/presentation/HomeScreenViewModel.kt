package com.example.restaurantapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.util.sha256
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ApiApi
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ServerError
import org.openapitools.client.infrastructure.Success
import org.openapitools.client.models.UserLogin
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiApi
): ViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login(onSuccess: (String, String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.apiUsersLoginCreateWithHttpInfo(UserLogin(_username.value, _password.value.sha256()))
                }

                when (response) {
                    is Success -> {
                        val username = response.data?.username
                        val firstName = response.data?.firstName
                        val lastName = response.data?.lastName
                        onSuccess(username.toString(), firstName.toString(), lastName.toString())
                    }

                    is ClientError -> {
                        onError(response.body.toString())
                    }

                    is ServerError -> {
                        onError(response.body.toString())
                    }
                }
//                val asd = response.statusCode

            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}
