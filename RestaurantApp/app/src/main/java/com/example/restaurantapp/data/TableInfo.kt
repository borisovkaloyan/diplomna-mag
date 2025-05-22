package com.example.restaurantapp.data

data class TableInfo(
    val devices: List<DeviceInfo>,
    val connectionState: ConnectionState,
    val topDevice: DeviceInfo? = null // optional
)

