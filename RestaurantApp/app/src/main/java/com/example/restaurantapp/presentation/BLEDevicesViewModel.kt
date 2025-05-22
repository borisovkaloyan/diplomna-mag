package com.example.restaurantapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.data.BLEDataReceiveManager
import com.example.restaurantapp.data.DeviceInfo
import com.example.restaurantapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BLEViewModel @Inject constructor(
    private val bleManager: BLEDataReceiveManager
) : ViewModel() {

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
}
