package dev.nordix.irbridge.ble.domain

import android.Manifest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NBleClient {
    val isConnected: StateFlow<Boolean>

    @RequiresPermission(allOf = [
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    ])
    fun getFirstAndListen(
        scanTimeoutMs: Long = 5_000L,
        reconnectDelayMs: Long = 1_000L,
        resetAddressOnFailure: Boolean = true,
    ): Flow<IntArray>
}
