package com.example.restaurantapp.data

import com.example.restaurantapp.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface BLEDataReceiveManager {

    val data: MutableSharedFlow<Resource<TableInfo>>

    fun discover()

}