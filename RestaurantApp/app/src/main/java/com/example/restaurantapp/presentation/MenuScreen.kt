package com.example.restaurantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.restaurantapp.util.fullImageUrl
import org.openapitools.client.models.CategoryEnum
import org.openapitools.client.models.MenuItem

@Composable
fun MenuScreen(
    userId: Int,
    firstName: String,
    lastName: String,
    baseUrl: String,
    viewModel: MenuViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("Menu") }
    var selectedCategory by remember { mutableStateOf<CategoryEnum?>(null) }

    val items by viewModel.menuItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(selectedCategory) {
        selectedCategory?.let {
            viewModel.loadItemsByCategory(it.name) // use enum name as category string
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(20.dp))

        Text(
            text = "Категории",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Show categories
        CategoryEnum.values().forEach { category ->
            Text(
                text = category.value,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedCategory = category
                    }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Show loading or error
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            error != null -> {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            selectedCategory != null -> {
                Text(
                    text = "Продукти в категория ${selectedCategory!!.value}:",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                items.forEach { item ->
                    MenuItemCard(
                        item = item,
                        baseUrl = baseUrl,
                        onAddToCart = { viewModel.addToCart(it) }
                    )
                }
            }
        }
    }
}


@Composable
fun MenuItemCard(
    item: MenuItem,
    onAddToCart: (MenuItem) -> Unit,
    baseUrl: String
) {
    val fullImageUrl = baseUrl.trimEnd('/') + item.image.toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = fullImageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.isVegetarian == true) {
                        Text(text = "🌿", fontSize = 16.sp, color = Color(0xFF4CAF50))
                    }
                }

                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Цена: ${item.price}лв",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = { onAddToCart(item) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добави в количка",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
