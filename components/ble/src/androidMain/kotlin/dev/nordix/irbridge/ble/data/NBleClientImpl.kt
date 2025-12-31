package dev.nordix.irbridge.ble.data

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import dev.nordix.irbridge.ble.domain.NBleClient
import dev.nordix.irbridge.ble.model.FrameAcc
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

@Suppress("MagicNumber")
internal class NBleClientImpl(
    private val context: Context,
    private val serviceUuid: UUID,
    private val notifyCharUuid: UUID,
) : NBleClient {
    private val bluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val adapter by lazy { bluetoothManager.adapter }

    private var acc: FrameAcc? = null

    private val activeGatt = AtomicReference<BluetoothGatt?>(null)
    private val activeReady = AtomicReference<CompletableDeferred<Unit>?>(null)

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected

    internal fun Int.roundToStep(step: Int): Int {
        require(step > 0)
        return ((this + step / 2) / step) * step
    }

    /**
     * Возвращает собранный кадр длительностей (микросекунды), когда приходит END.
     * Иначе возвращает null.
     */
    private fun onIrNotify(bytes: ByteArray): IntArray? {
        if (bytes.isEmpty()) return null

        when (bytes[0].toInt() and 0xFF) {
            0xA0 -> { // BEGIN
                Log.d(TAG, "BEGIN: ${bytes.contentToString()}")
                if (bytes.size < 5) return null
                val fid = bytes[1].toInt() and 0xFF
                val count = (bytes[2].toInt() and 0xFF) or ((bytes[3].toInt() and 0xFF) shl 8)
                val unit = bytes[4].toInt() and 0xFF

                acc = FrameAcc(fid = fid, expectedCount = count, unit = unit)
                return null
            }

            0xA1 -> { // CHUNK
                Log.d(TAG, "CHUNK: ${bytes.contentToString()}")
                val a = acc ?: return null
                if (bytes.size < 8) return null

                val fid = bytes[1].toInt() and 0xFF
                if (fid != a.fid) return null

                val seq = bytes[2].toInt() and 0xFF
                val len = bytes[3].toInt() and 0xFF
                if (len == 0) return null
                if (len % 2 != 0) return null
                if (bytes.size < 4 + len) return null

                // Дубликаты seq игнорируем
                if (a.chunks[seq] == null) {
                    a.chunks[seq] = bytes.copyOfRange(4, 4 + len)
                    a.receivedChunkCount++
                }
                return null
            }

            0xA2 -> { // END
                Log.d(TAG, "END: ${bytes.contentToString()}")
                val a = acc ?: return null
                if (bytes.size < 3) return null

                val fid = bytes[1].toInt() and 0xFF
                if (fid != a.fid) return null

                val chunksTotal = bytes[2].toInt() and 0xFF

                // unit = microseconds
                if (a.unit != 1) { acc = null; return null }

                // Должны получить все чанки 0..chunksTotal-1
                for (s in 0 until chunksTotal) {
                    if (a.chunks[s] == null) {
                        acc = null
                        return null
                    }
                }

                // Собираем durations в порядке seq
                val durations = IntArray(a.expectedCount)
                var writePos = 0

                for (s in 0 until chunksTotal) {
                    val payload = a.chunks[s]!!
                    var p = 0
                    while (p < payload.size) {
                        if (writePos >= durations.size) {
                            acc = null
                            return null // перелив — значит протокол/len/count не совпал
                        }
                        val lo = payload[p].toInt() and 0xFF
                        val hi = payload[p + 1].toInt() and 0xFF
                        durations[writePos++] = (lo or (hi shl 8)).roundToStep(ROUNDING_STEP)
                        p += 2
                    }
                }

                // Должны заполнить ровно expectedCount
                if (writePos != a.expectedCount) {
                    acc = null
                    return null
                }

                acc = null
                return durations
            }

            else -> return null
        }
    }

    @RequiresPermission(allOf = [
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    ])
    override fun getFirstAndListen(
        scanTimeoutMs: Long,
        reconnectDelayMs: Long,
        resetAddressOnFailure: Boolean,
    ): Flow<IntArray> = callbackFlow {
        val scanner = adapter.bluetoothLeScanner
        var lastAddress: String? = null

        suspend fun scanForFirstAddress(): String {
            val deferred = CompletableDeferred<String>()

            val scanCallback = object : ScanCallback() {
                @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    val device = result.device
                    val name = device.name
                    if (
                        name == null
                        || !name.startsWith(TARGET_NAME_PREFIX, ignoreCase = true)
                        || deferred.isCompleted
                    ) return
                    deferred.complete(device.address)
                }

                override fun onScanFailed(errorCode: Int) {
                    if (!deferred.isCompleted) {
                        deferred.completeExceptionally(
                            IllegalStateException("Scan failed with code $errorCode")
                        )
                    }
                }
            }

            try {
                scanner.startScan(scanCallback)
                val timeoutJob = launch {
                    delay(scanTimeoutMs)
                    if (!deferred.isCompleted) {
                        deferred.completeExceptionally(
                            IllegalStateException("No device found within ${scanTimeoutMs}ms")
                        )
                    }
                }

                return try {
                    deferred.await()
                } finally {
                    timeoutJob.cancel()
                    try {
                        scanner.stopScan(scanCallback)
                    } catch (_: Throwable) {}
                }
            } catch (t: Throwable) {
                try {
                    scanner.stopScan(scanCallback)
                } catch (_: Throwable) {}
                throw t
            }
        }

        val loopJob = launch {
            while (isActive) {
                try {
                    val address = lastAddress ?: scanForFirstAddress().also { lastAddress = it }
                    connectAndListen(address).collect { packet ->
                        trySend(packet).isSuccess
                    }
                    delay(reconnectDelayMs)
                } catch (t: Throwable) {
                    Log.w(TAG, "getFirstAndListen: reconnect loop error: ${t.message}", t)

                    if (resetAddressOnFailure) {
                        lastAddress = null
                    }
                    delay(reconnectDelayMs)
                }
            }
        }

        awaitClose {
            loopJob.cancel()
            try {
                scanner.stopScan(object : ScanCallback() {})
            } catch (_: Throwable) {}
            closeActiveGatt()
        }
    }

    /**
     * Подключение + listen notify. Возвращает поток "сырых" пакетов.
     * Здесь же включаем notifications и считаем "готово" после успешной записи CCCD.
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun connectAndListen(address: String): Flow<IntArray> = callbackFlow {
        // На всякий — закрываем прошлое соединение
        closeActiveGatt()

        val device = adapter.getRemoteDevice(address)
        val ready = CompletableDeferred<Unit>()
        activeReady.set(ready)

        val cb = object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                Log.d(TAG, "Connection state change status=$status newState=$newState")

                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        // Важно: если status != GATT_SUCCESS, лучше завершать
                        if (status != BluetoothGatt.GATT_SUCCESS) {
                            close(IllegalStateException("GATT connect failed status=$status"))
                            return
                        }
                        _isConnected.value = true
                        gatt.requestMtu(247)
                        gatt.discoverServices()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        // Закрываем поток
                        close()
                        _isConnected.value = false
                    }
                }
            }

            @Suppress("ReturnCount")
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    close(IllegalStateException("Service discovery failed status=$status"))
                    return
                }

                val service = gatt.getService(serviceUuid)
                if (service == null) {
                    close(IllegalStateException("Service $serviceUuid not found"))
                    return
                }

                val chr = service.getCharacteristic(notifyCharUuid)
                if (chr == null) {
                    close(IllegalStateException("Notify characteristic $notifyCharUuid not found"))
                    return
                }

                if (!enableNotifications(gatt, chr)) {
                    close(IllegalStateException("Failed to enable notifications (local)"))
                    return
                }

                val cccd = chr.getDescriptor(CCCD_UUID)
                if (cccd == null) {
                    close(IllegalStateException("CCCD descriptor not found"))
                    return
                }

                cccd.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                val ok = gatt.writeDescriptor(cccd)
                if (!ok) {
                    close(IllegalStateException("writeDescriptor(CCCD) returned false"))
                    return
                }
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor,
                status: Int
            ) {
                if (descriptor.uuid == CCCD_UUID) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        Log.d(TAG, "CCCD enabled, ready")
                        activeReady.get()?.complete(Unit)
                    } else {
                        close(IllegalStateException("CCCD write failed status=$status"))
                    }
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                if (characteristic.uuid == notifyCharUuid) {
                    onIrNotify(value)?.let { trySend(it).isSuccess }
                }
            }
        }

        // autoConnect=false: ожидаем быстрый connect (для твоего сценария “подключиться и слушать”)
        val gatt = device.connectGatt(context, /* autoConnect = */ false, cb)
        activeGatt.set(gatt)

        // Если нужно где-то ждать готовность — можно наружу вернуть ready через отдельный API.
        // Здесь же просто запускаем поток notify; по желанию можно не эмитить, пока ready не complete().

        awaitClose {
            closeActiveGatt()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun enableNotifications(gatt: BluetoothGatt, chr: BluetoothGattCharacteristic): Boolean {
        return gatt.setCharacteristicNotification(chr, true)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun closeActiveGatt() {
        val gatt = activeGatt.getAndSet(null) ?: return
        try {
            gatt.disconnect()
        } catch (_: Throwable) {}
        try {
            gatt.close()
        } catch (_: Throwable) {}
        activeReady.getAndSet(null)
    }

    companion object {
        private const val TAG = "IRBlePacketsClient"
        private const val TARGET_NAME_PREFIX = "IR-Capture"
        private val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

        private const val ROUNDING_STEP = 100
    }
}
