package dev.nordix.irbridge.ble.di

import android.content.Context
import dev.nordix.irbridge.ble.data.NBleClientImpl
import dev.nordix.irbridge.ble.data.PermissionsHelper
import dev.nordix.irbridge.ble.domain.NBleClient
import dev.nordix.irbridge.core.utils.OnAppLaunchedHandler
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.UUID

val bleModule = module {
    single<NBleClient> {
        NBleClientImpl(
            context = get(),
            serviceUuid = UUID.fromString("900e11d5-336a-0c9b-1f4f-529e2a8c217b"),
            notifyCharUuid = UUID.fromString("910e11d5-336a-0c9b-1f4f-529e2a8c217b")
        )
    }

    single {
        PermissionsHelper(context = get())
    }
}
