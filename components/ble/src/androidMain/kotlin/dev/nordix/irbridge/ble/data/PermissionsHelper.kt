package dev.nordix.irbridge.ble.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionsHelper(
    private val context: Context,
) {

    lateinit var enableBtLauncher: ActivityResultLauncher<Intent>
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val _bleReady = MutableStateFlow(false)
    val bleReady: StateFlow<Boolean> = _bleReady.asStateFlow()

    fun registerLaunchers(activity: ComponentActivity) {

        enableBtLauncher = activity.registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { /* no-op */ }

        permissionLauncher = activity.registerForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { grants ->
                val allGranted = grants.values.all { it }
                if (allGranted) {
                    ensureBluetoothEnabled()
                }
            }
    }
    fun requestBlePermissionsIfNeeded() {
        val perms = requiredPermissions()
        val missing = perms.filter { p ->
            ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            ensureBluetoothEnabled()
            _bleReady.value = true
        } else {
            permissionLauncher.launch(missing.toTypedArray())
            _bleReady.value = false
        }
    }

    fun checkBlePermissionsIfNeeded() {
        val perms = requiredPermissions()
        val missing = perms.filter { p ->
            ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            _bleReady.value = true
        }
    }

    fun requiredPermissions(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    }

    fun ensureBluetoothEnabled() {
        val manager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return

        if (!adapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtLauncher.launch(intent)
        }
    }
}
