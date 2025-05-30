package com.example.restaurantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.net.URLEncoder

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(20.dp))

        Text("Register", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        val scope = rememberCoroutineScope()

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            if (password != confirmPassword) {
                responseMessage = "Passwords do not match"
                return@Button
            }

            responseMessage = null

            viewModel.registerUser(
                username = username,
                email = email,
                firstName = firstName,
                lastName = lastName,
                password = password,
                onSuccess = { msg ->
                    val encodedMessage = URLEncoder.encode(msg, "UTF-8")
                    navController.navigate("home_screen?message=$encodedMessage") {
                        popUpTo("home_screen") { inclusive = true }
                    }
                },
                onError = { error ->
                    responseMessage = error
                }
            )
        }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        responseMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

