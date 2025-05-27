package com.example.restaurantapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ApiApi
import org.openapitools.client.models.MenuItem
import org.openapitools.client.models.OrderCategory
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor (
    private val api: ApiApi
) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cartItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val cartItems: StateFlow<List<MenuItem>> = _cartItems

    val groupedCartItems: StateFlow<Map<Int, Pair<MenuItem, Int>>> = _cartItems
        .map { list ->
            list.groupBy { it.id }
                .mapValues { entry ->
                    val firstItem = entry.value.first()
                    val count = entry.value.size
                    firstItem to count
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    fun addToCart(item: MenuItem) {
        _cartItems.value = _cartItems.value + item
    }

    fun loadItemsByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val items = withContext(Dispatchers.IO) {
                    api.apiMenuItemsItemsByCategoryCreate(OrderCategory(category))
                }
                _menuItems.value = items
            } catch (e: Exception) {
                _error.value = "Failed to load items: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
