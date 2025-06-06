package com.example.restaurantapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.openapitools.client.models.MenuItem
import org.openapitools.client.models.Order
import org.openapitools.client.models.OrderItem

@Composable
fun OrderDetailsScreen(
    baseUrl: String,
    viewModel: MenuViewModel
) {
    val order = viewModel.selectedOrder.collectAsState().value

    if (order == null) {
        Text("Зареждане...")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(20.dp))

        Text("Идентификатор на поръчка: ${order.id}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        Text("Дата: ${order.orderDate.toLocalDate()}", color = MaterialTheme.colorScheme.onBackground)
        Text("Адрес: ${order.deliveryAddress}", color = MaterialTheme.colorScheme.onBackground)
        Text("Сума: ${order.totalAmount ?: "N/A"}лв.", color = MaterialTheme.colorScheme.onBackground)
        Text("Статус: ${order.status ?: "Неизвестен"}", color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Артикули:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)

        if (order.orderItems.isEmpty()) {
            Text("Зареждане на артикули...", color = MaterialTheme.colorScheme.onBackground)
        } else {
            order.orderItems.forEach { item ->

                val fullImageUrl = baseUrl.trimEnd('/') + item.menuItem.image.toString()

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
                            contentDescription = item.menuItem.name,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp)
                        )
                        Text(
                            "• ${item.quantity} x ${item.menuItem.name} - ${
                                item.menuItem.price.multiply((item.quantity ?: 1L).toBigDecimal())
                            }лв."
                        )
                    }
                }
            }
        }
    }
}
