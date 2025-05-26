package com.example.restaurantapp.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.restaurantapp.data.BLEDataReceiveManager
import com.example.restaurantapp.data.ble.BLEDataReceiveManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.openapitools.client.apis.ApiApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides
    @Singleton
    fun provideBLEDataReceiveManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): BLEDataReceiveManager {
        return BLEDataReceiveManagerImpl(bluetoothAdapter, context)
    }

    @Provides
    @Singleton
    fun provideApiApi(): ApiApi {
        return ApiApi(basePath = "http://192.168.0.183:8000")
    }

}