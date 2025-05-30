package com.example.restaurantapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.data.BLEDataReceiveManager
import com.example.restaurantapp.data.DeviceInfo
import com.example.restaurantapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ApiApi
import org.openapitools.client.models.MenuItem
import org.openapitools.client.models.Order
import org.openapitools.client.models.OrderCategory
import org.openapitools.client.models.OrderRequest
import org.openapitools.client.models.OrdersByUser
import javax.inject.Inject
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import java.util.Locale

@HiltViewModel
class MenuViewModel @Inject constructor (
    private val bleManager: BLEDataReceiveManager,
    private val api: ApiApi
) : ViewModel() {

    // location shit
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun initLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLastKnownLocation(context: Context, onLocationReceived: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onLocationReceived("Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            val address = if (!addresses.isNullOrEmpty()) {
                                val addr = addresses[0]
                                // You can use just the address line or construct something more specific
                                addr.getAddressLine(0)
                            } else {
                                "Err: Unknown location"
                            }

                            withContext(Dispatchers.Main) {
                                onLocationReceived(address)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                onLocationReceived("Err: Geocoding failed: ${e.localizedMessage}")
                            }
                        }
                    }
                } else {
                    onLocationReceived("Err: Location not available")
                }
            }
            .addOnFailureListener {
                onLocationReceived("Err: Location fetch failed")
            }
    }

    // ble shit
    private val _devices = MutableStateFlow<List<DeviceInfo>>(emptyList())
    val devices: StateFlow<List<DeviceInfo>> = _devices

    private val _topDevice = MutableStateFlow<DeviceInfo?>(null)
    val topDevice: StateFlow<DeviceInfo?> = _topDevice

    private val _message = MutableStateFlow<String>("")
    val message: StateFlow<String> = _message

    private fun collectScanResults() {
        viewModelScope.launch {
            bleManager.data.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _devices.value = resource.data.devices
                        _topDevice.value = resource.data.topDevice
                    }
                    is Resource.Loading -> {
                        _message.value = resource.message ?: ""
                    }
                    is Resource.Error -> {
                        _message.value = resource.errorMessage
                    }
                }
            }
        }
    }

    fun scan() {
        collectScanResults()
        bleManager.discover() // Start scanning
    }

    // other shit

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cartItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val cartItems: StateFlow<List<MenuItem>> = _cartItems

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    val selectedOrder = MutableStateFlow<Order?>(null)

    suspend fun getMenuItemById(itemId: Int): MenuItem? {
        return try {
            withContext(Dispatchers.IO) {
                api.apiMenuItemsGetItemByIdRetrieve(itemId)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun loadOrdersForUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.apiOrdersOrdersByUserCreate(OrdersByUser(userId))
                }
                _orders.value = response
            } catch (e: Exception) {
                _orders.value = emptyList()
            }
        }
    }

    fun addToCart(item: MenuItem) {
        _cartItems.value = _cartItems.value + item
    }

    fun removeFromCart(item: MenuItem) {
        _cartItems.value = _cartItems.value.toMutableList().also {
            val index = it.indexOfFirst { it.id == item.id }
            if (index != -1) it.removeAt(index)
        }
    }

    fun createOrder(order: OrderRequest, context: Context) {
        viewModelScope.launch {
            try {
                val createdOrder = withContext(Dispatchers.IO) {
                    api.apiOrdersCreateOrderCreate(order)
                }
                Toast.makeText(context, "Order created! \nStatus: ${createdOrder.status}", Toast.LENGTH_LONG)
                    .show()

                clearCart()

            } catch (e: Exception) {
                Toast.makeText(context, "Error creating order: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
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
