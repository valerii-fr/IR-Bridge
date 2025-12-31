package dev.nordix.irbridge.ir.data

import android.content.Context
import android.hardware.ConsumerIrManager

/**
 * Отправляет IR кадр (durations в микросекундах) через ConsumerIrManager.
 *
 * durationsUs: [on0, off0, on1, off1, ...] в микросекундах.
 * carrierFrequencyHz: обычно 38000 (38 kHz).
 */
@Suppress("ComplexCondition")
internal fun Context.transmitIrFrame(
    durationsUs: IntArray,
    carrierFrequencyHz: Int = 38_000
): Boolean {
    val ir = getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager
    if (
        ir == null
        || !ir.hasIrEmitter()
        || durationsUs.isEmpty()
        || durationsUs.any { it <= 0 }
    ) return false

    ir.transmit(carrierFrequencyHz, durationsUs)
    return true
}
