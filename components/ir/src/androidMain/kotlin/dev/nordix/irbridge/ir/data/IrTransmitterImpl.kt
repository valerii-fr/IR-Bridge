package dev.nordix.irbridge.ir.data

import android.content.Context
import android.hardware.ConsumerIrManager
import dev.nordix.irbridge.ir.domain.IrTransmitter

class IrTransmitterImpl(
    private val context: Context,
) : IrTransmitter {

    override fun transmitFrame(durationsUs: IntArray) {
        context.transmitIrFrame(durationsUs)
    }

    override fun hasIrTransmitter(): Boolean {
        val ir = context.getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager
        return ir?.hasIrEmitter() ?: false
    }

}
