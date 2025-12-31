package dev.nordix.irbridge.ir.di

import dev.nordix.irbridge.ir.data.IrTransmitterImpl
import dev.nordix.irbridge.ir.domain.IrTransmitter
import org.koin.dsl.module

val irModule = module {
    single<IrTransmitter> {
        IrTransmitterImpl(context = get())
    }
}
