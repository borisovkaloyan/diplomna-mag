import android.bluetooth.BluetoothAdapter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantapp.presentation.MenuViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.restaurantapp.data.DeviceInfo
import com.example.restaurantapp.presentation.DeviceItem
import com.example.restaurantapp.presentation.permissions.PermissionUtils
import com.example.restaurantapp.presentation.permissions.SystemBroadcastReceiver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.openapitools.client.models.MenuItem
import org.openapitools.client.models.Order
import org.openapitools.client.models.OrderRequest
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CartScreen(
    userId: Int,
    firstName: String,
    lastName: String,
    onBluetoothStateChanged: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    var total = java.math.BigDecimal.ZERO
    val context = LocalContext.current

    var showSheet by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }


    // ble shit
    val devices by viewModel.devices.collectAsState()
    val message by viewModel.message.collectAsState()

    val permissionState = rememberMultiplePermissionsState(PermissionUtils.permissions)

    // Listen for Bluetooth state changes
    SystemBroadcastReceiver(BluetoothAdapter.ACTION_STATE_CHANGED) { intent ->
        val action = intent?.action ?: return@SystemBroadcastReceiver
        if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            onBluetoothStateChanged()
        }
    }

    // Request permissions when this screen first shows
    LaunchedEffect(Unit) {
        onBluetoothStateChanged()
        viewModel.initLocationClient(context)
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(20.dp))

        // ble shit
        // Show permission rationale or error if needed
        if (!permissionState.allPermissionsGranted) {
            Text(
                text = "Необходими са Bluetooth разрешения.",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Дай разрешения")
            }
            return@Column
        }

        // Start scanning only after permissions are granted
        LaunchedEffect(permissionState.allPermissionsGranted) {
            if (permissionState.allPermissionsGranted) {
                viewModel.scan()
            }
        }


        Text("Кошница на $firstName $lastName", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (cartItems.isEmpty()) {
            Text("Кошницата е празна.", modifier = Modifier.padding(top = 16.dp))
        } else {
            val groupedItems = cartItems.groupBy { it.id }

            groupedItems.forEach { (_, items) ->
                val item = items.first()
                val count = items.size
                total += item.price.multiply(count.toBigDecimal())

                CartItemCard(
                    item = item,
                    count = count,
                    onIncrease = { viewModel.addToCart(item) },
                    onDecrease = { viewModel.removeFromCart(item) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Text(
                text = "Сума: ${total}лв.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
            )

            Button(
                onClick = {
                    showSheet = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Подай поръчка")
            }

            if (showSheet) {

                LaunchedEffect(Unit) {
                    var best_device: DeviceInfo? = null
                    devices.forEach { device ->
                        if (device.deviceName.startsWith("Table_")) {
                            if (best_device == null) {
                                best_device = device
                            }
                            else if (best_device.rssi < device.rssi) {
                                best_device = device
                            }
                        }
                    }

                    if (best_device != null) {
                        address = "Маса ${best_device.deviceName.substringAfter('_')}"
                    } else {
                        viewModel.getLastKnownLocation(context) { location ->
                            if (location.startsWith("Err:")) {
                                // Show a message or re-request permission
                                Toast.makeText(context, "Не успяхме да определим местоположение или номер на маса", Toast.LENGTH_SHORT).show()
                                return@getLastKnownLocation
                            } else {
                                address = location
                            }
                        }
                    }
                }

                var tempAddress by remember { mutableStateOf("") }

                ModalBottomSheet(
                    onDismissRequest = { showSheet = false }
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Сума: ${total}лв.", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        if (address.isEmpty()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Въведете адрес ръчно:", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = tempAddress,
                                    onValueChange = { tempAddress = it },
                                    placeholder = { Text("Адрес") },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                                )
                            }
                        } else {
                            Text(
                                "Вашата локация: $address",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showSheet = false
                            val userId = userId
                            val items = cartItems.map { it.id }

                            if (address == "") {
                                address = tempAddress
                            }

                            if (address != "") {
                                val order = OrderRequest(
                                    deliveryAddress = address,
                                    user = userId,
                                    items = items
                                )
                                viewModel.createOrder(order, context)
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Плати сега")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: MenuItem,
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = item.description, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease, enabled = count > 1) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Намали")
                }
                Text(
                    text = count.toString(),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrease) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Увеличи")
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Сума: ${item.price.multiply(count.toBigDecimal())}лв.",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
