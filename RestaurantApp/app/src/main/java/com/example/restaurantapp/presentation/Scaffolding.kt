package com.example.restaurantapp.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavHostController,
    userId: Int,
    firstName: String,
    lastName: String
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == "Menu",
            onClick = {
                onTabSelected("Menu")
                navController.navigate(Screen.MenuScreen.createRoute(userId, firstName, lastName))
            },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
            label = { Text("Menu") }
        )
        NavigationBarItem(
            selected = selectedTab == "User",
            onClick = {
                onTabSelected("User")
                navController.navigate(Screen.UserScreen.createRoute(userId, firstName, lastName))
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "User") },
            label = { Text("User") }
        )
        NavigationBarItem(
            selected = selectedTab == "Cart",
            onClick = {
                onTabSelected("Cart")
                navController.navigate(Screen.CartScreen.createRoute(userId, firstName, lastName))
            },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
    }
}

@Composable
fun MainScaffold(
    navController: NavHostController,
    currentScreen: String,
    userId: Int,
    firstName: String,
    lastName: String,
    content: @Composable ((PaddingValues) -> Unit)
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentScreen,
                onTabSelected = { tab ->
                    when (tab) {
                        "Menu" -> navController.navigate(Screen.MenuScreen.createRoute(userId, firstName, lastName)) {
                            popUpTo(Screen.MainGraph.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        "Cart" -> navController.navigate(Screen.CartScreen.createRoute(userId, firstName, lastName)) {
                            popUpTo(Screen.MainGraph.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        // Add User tab navigation here if needed
                    }
                },
                navController = navController,
                userId = userId,
                firstName = firstName,
                lastName = lastName
            )
        }
    ) { padding ->
        content(padding)
    }
}

