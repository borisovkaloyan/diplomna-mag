package com.example.restaurantapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    onBluetoothStateChanged: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Screen.BLEDevices.route) {
            BLEDevices(onBluetoothStateChanged)
        }
    }
}

sealed class Screen(val route: String) {
    data object HomeScreen:Screen("home_screen")
    data object BLEDevices:Screen("ble_devices")
}