package com.example.restaurantapp.presentation

import CartScreen
import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.openapitools.client.models.Order

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

//        composable(Screen.BLEDevices.route) {
//            BLEDevices(onBluetoothStateChanged)
//        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.OrderDetails.route
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(Screen.MainGraph.route)
            }
            val viewModel: MenuViewModel = hiltViewModel(parentEntry)

            OrderDetailsScreen(baseUrl, viewModel = viewModel)
        }

        // NESTED GRAPH for Menu/Cart
        navigation(
            startDestination = Screen.MenuScreen.route,
            route = Screen.MainGraph.route
        ) {
            composable(
                Screen.MenuScreen.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("firstName") { type = NavType.StringType },
                    navArgument("lastName") { type = NavType.StringType }
                )
            ) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Screen.MainGraph.route)
                }
                val userId = entry.arguments?.getInt("userId") ?: 0
                val firstName = entry.arguments?.getString("firstName") ?: ""
                val lastName = entry.arguments?.getString("lastName") ?: ""
                val viewModel: MenuViewModel = hiltViewModel(parentEntry)

                MainScaffold(
                    navController = navController,
                    currentScreen = "Menu",
                    userId = userId,
                    firstName = firstName,
                    lastName = lastName
                ) { padding ->
                    MenuScreen(
                        userId = userId,
                        firstName = firstName,
                        lastName = lastName,
                        baseUrl = baseUrl,
                        viewModel=viewModel
                    )
                }
            }


            composable(
                Screen.CartScreen.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("firstName") { type = NavType.StringType },
                    navArgument("lastName") { type = NavType.StringType }
                )
            ) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Screen.MainGraph.route)
                }
                val userId = entry.arguments?.getInt("userId") ?: 0
                val firstName = entry.arguments?.getString("firstName") ?: ""
                val lastName = entry.arguments?.getString("lastName") ?: ""
                val viewModel: MenuViewModel = hiltViewModel(parentEntry)

                MainScaffold(
                    navController = navController,
                    currentScreen = "Cart",
                    userId = userId,
                    firstName = firstName,
                    lastName = lastName
                ) { padding ->
                    CartScreen(
                        userId = userId,
                        firstName = firstName,
                        lastName = lastName,
                        onBluetoothStateChanged = onBluetoothStateChanged,
                        viewModel = viewModel
                    )
                }
            }

            composable(
                Screen.UserScreen.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("firstName") { type = NavType.StringType },
                    navArgument("lastName") { type = NavType.StringType }
                )
            ) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Screen.MainGraph.route)
                }

                val userId = entry.arguments?.getInt("userId") ?: 0
                val firstName = entry.arguments?.getString("firstName") ?: ""
                val lastName = entry.arguments?.getString("lastName") ?: ""
                val viewModel: MenuViewModel = hiltViewModel(parentEntry)
                LaunchedEffect(userId) {
                    viewModel.loadOrdersForUser(userId)
                }

                MainScaffold(
                    navController = navController,
                    currentScreen = "User",
                    userId = userId,
                    firstName = firstName,
                    lastName = lastName
                ) { padding ->
                    UserScreen(
                        userId = userId,
                        firstName = firstName,
                        lastName = lastName,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object MainGraph : Screen("main_graph")
    data object HomeScreen : Screen("home_screen?message={message}")
//    data object BLEDevices : Screen("ble_devices")
    data object MenuScreen : Screen("menu_screen/{userId}/{firstName}/{lastName}") {
        fun createRoute(userId: Int, firstName: String, lastName: String) =
            "menu_screen/$userId/$firstName/$lastName"
    }
    data object Register : Screen("register")
    data object CartScreen : Screen("cart_screen/{userId}/{firstName}/{lastName}") {
        fun createRoute(userId: Int, firstName: String, lastName: String) =
            "cart_screen/$userId/$firstName/$lastName"
    }
    data object UserScreen : Screen("user_screen/{userId}/{firstName}/{lastName}") {
        fun createRoute(userId: Int, firstName: String, lastName: String) =
            "user_screen/$userId/$firstName/$lastName"
    }
    data object OrderDetails : Screen("order_details")

}
