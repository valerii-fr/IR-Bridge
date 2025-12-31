package dev.nordix.irbridge.ir.domain

interface IrTransmitter {
    fun transmitFrame(durationsUs: IntArray)
    fun hasIrTransmitter() : Boolean
}
