package dev.nordix.irbridge

import android.app.Application
import dev.nordix.irbridge.ble.di.bleModule
import dev.nordix.irbridge.core.utils.OnAppLaunchedHandler
import dev.nordix.irbridge.feature.widget.di.widgetModule
import dev.nordix.irbridge.ir.di.irModule
import dev.nordix.irbridge.remotes.di.remotesFeatureModule
import dev.nordix.irbridge.remotes.di.remotesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                bleModule,
                remotesModule,
                remotesFeatureModule,
                irModule,
                widgetModule,
            )
        }

        val handlers: List<OnAppLaunchedHandler> = getKoin().getAll()
        handlers.forEach(OnAppLaunchedHandler::invoke)
    }

}
