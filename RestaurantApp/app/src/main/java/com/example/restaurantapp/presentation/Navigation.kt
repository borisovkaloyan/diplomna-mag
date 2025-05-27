package com.example.restaurantapp.presentation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(
    onBluetoothStateChanged: () -> Unit,
    baseUrl: String
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(
            route = Screen.HomeScreen.route,
            arguments = listOf(
                navArgument("message") { defaultValue = ""; nullable = true }
            )
        ) {
            HomeScreen(navController)
        }

        composable(Screen.BLEDevices.route) {
            BLEDevices(onBluetoothStateChanged)
        }

        composable(
            route = Screen.MenuScreen.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""

            MenuScreen(baseUrl)
//            MenuScreen(username, firstName, lastName)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
    }
}

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen?message={message}")
    data object BLEDevices : Screen("ble_devices")
    data object MenuScreen : Screen("menu_screen/{username}/{firstName}/{lastName}") {
        fun createRoute(username: String, firstName: String, lastName: String) =
            "menu_screen/$username/$firstName/$lastName"
    }
    data object Register : Screen("register")
    data object Cart : Screen("cart")
}