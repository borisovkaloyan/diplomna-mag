package com.example.restaurantapp.data.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.restaurantapp.data.BLEDataReceiveManager
import com.example.restaurantapp.data.DeviceInfo
import com.example.restaurantapp.data.TableInfo
import com.example.restaurantapp.data.ConnectionState
import com.example.restaurantapp.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BLEDataReceiveManagerImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) : BLEDataReceiveManager {

    override val data: MutableSharedFlow<Resource<TableInfo>> = MutableSharedFlow()

    private val devices = mutableMapOf<String, Int>() // deviceName -> rssi
    private var topDevice: DeviceInfo? = null

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null
    private var isScanning = false
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val deviceName = result.device.name ?: return
            val rssi = result.rssi

            if (deviceName !in devices || rssi > devices[deviceName]!!) {
                devices[deviceName] = rssi

                if (topDevice == null || rssi > topDevice!!.rssi) {
                    topDevice = DeviceInfo(deviceName, rssi)
                }

                Log.d("BLE", "Discovered: $deviceName with RSSI $rssi")

                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Found ${devices.size} devices..."))
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun discover() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning for BLE devices..."))

            isScanning = true
            bleScanner.startScan(null, scanSettings, scanCallback)

            delay(10_000)

            bleScanner.stopScan(scanCallback)
            isScanning = false

            val deviceList = devices.map { (name, rssi) ->
                DeviceInfo(deviceName = name, rssi = rssi)
            }

            val result = TableInfo(
                devices = ArrayList(deviceList),
                connectionState = ConnectionState.Disconnected, // or your appropriate state
                topDevice = topDevice
            )

            Log.d("BLE", "Top device: ${topDevice?.deviceName} with RSSI ${topDevice?.rssi}")

            data.emit(Resource.Success(data = result))
        }
    }
}
