package dev.nordix.irbridge.remotes.di

import dev.nordix.irbridge.remotes.screens.add.di.remoteAddFeatureModule
import dev.nordix.irbridge.remotes.screens.list.di.remotesListFeatureModule
import org.koin.dsl.module

val remotesFeatureModule = module {
    includes(
        remotesListFeatureModule,
        remoteAddFeatureModule,
    )
}
