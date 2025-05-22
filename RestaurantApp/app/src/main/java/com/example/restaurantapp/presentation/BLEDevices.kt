package com.example.restaurantapp.presentation

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantapp.data.DeviceInfo
import com.example.restaurantapp.presentation.permissions.SystemBroadcastReceiver

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restaurantapp.presentation.permissions.PermissionUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BLEDevices(
    onBluetoothStateChanged: () -> Unit,
    viewModel: BLEViewModel = hiltViewModel()
) {
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
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Show permission rationale or error if needed
        if (!permissionState.allPermissionsGranted) {
            Text(
                text = "Bluetooth permissions are required to scan for devices.",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("Grant Permissions")
            }
            return@Column
        }

        // Start scanning only after permissions are granted
        LaunchedEffect(permissionState.allPermissionsGranted) {
            if (permissionState.allPermissionsGranted) {
                viewModel.scan()
            }
        }

        if (message.isNotEmpty()) {
            Text(text = message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text("Discovered Devices:", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(devices) { device ->
                DeviceItem(device)
            }
        }
    }
}


@Composable
fun DeviceItem(device: DeviceInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column {
                Text("Name: ${device.deviceName}")
                Text("RSSI: ${device.rssi}")
            }
        }
    }
}
