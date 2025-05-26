import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantapp.presentation.LoginViewModel
import com.example.restaurantapp.presentation.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val rawMessage = navController
        .currentBackStackEntry
        ?.arguments
        ?.getString("message")
        ?.takeIf { it.isNotBlank() }

    val message = remember {
        mutableStateOf<String?>(null)
    }

    // Decode message from nav args only once
    LaunchedEffect(rawMessage) {
        message.value = rawMessage?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.name())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        message.value?.let {
            Text(
                text = it,
                color = if (it.contains("success", ignoreCase = true)) Color.Green else Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = username,
            onValueChange = { newText -> viewModel.onUsernameChanged(newText) },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { newText -> viewModel.onPasswordChanged(newText) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.login(
                onSuccess = { username: String, firstName: String, lastName: String ->
                    navController.navigate(Screen.UserProfile.createRoute(username, firstName, lastName)) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                },
                onError = { errorMessage ->
                    message.value = errorMessage
                    println("Login error: $errorMessage")
                }
            )
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate(Screen.Register.route)
        }) {
            Text("Register New User")
        }
    }
}
