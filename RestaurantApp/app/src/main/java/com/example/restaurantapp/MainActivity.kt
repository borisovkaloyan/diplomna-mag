package com.example.restaurantapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.restaurantapp.di.AppModule
import com.example.restaurantapp.di.AppModule.BaseUrl
import com.example.restaurantapp.presentation.Navigation
import com.example.restaurantapp.ui.theme.RestaurantAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Handler
import javax.inject.Inject

internal const val ACTION_PERMISSIONS_GRANTED = "BluetoothPermission.permissions_granted"
internal const val ACTION_PERMISSIONS_DENIED = "BluetoothPermission.permissions_denied"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter
    @Inject @BaseUrl lateinit var baseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantAppTheme {
                Navigation(
                    onBluetoothStateChanged = {
                        showBluetoothDialog()
                    },
                    baseUrl
                )
            }
        }
    }

    private val requestBluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showBluetoothDialog()
            } else {
                // Handle denial appropriately
            }
        }

    override fun onStart() {
        super.onStart()
        showBluetoothDialog()
        showLocationDialog()
    }

    private var isBluetoothDialogAlreadyShown = false
    private fun showBluetoothDialog() {
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            if (!bluetoothAdapter.isEnabled) {
                if (isBluetoothDialogAlreadyShown) {
                    return
                }

                isBluetoothDialogAlreadyShown = true
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startBluetoothIntentForResult.launch(enableBluetoothIntent)
            }
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                isBluetoothDialogAlreadyShown = false
                if (result.resultCode != Activity.RESULT_OK) {
                    showBluetoothDialog()
                }
        }

    private fun showLocationDialog() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            checkIfLocationIsEnabled()
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkIfLocationIsEnabled()
        } else {
            Toast.makeText(this, "Location permission is required for Bluetooth scanning", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkIfLocationIsEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            AlertDialog.Builder(this)
                .setTitle("Enable Location")
                .setMessage("Location services are required for Bluetooth scanning. Please enable location.")
                .setPositiveButton("Enable") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
