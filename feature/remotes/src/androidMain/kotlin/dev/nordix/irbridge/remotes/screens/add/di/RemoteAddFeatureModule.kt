package dev.nordix.irbridge.remotes.screens.add.di

import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.screens.add.model.RemoteAddViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val remoteAddFeatureModule = module {
    viewModel { (remote: Remote) ->
        RemoteAddViewModel(
            remote = remote,
            remotesRepository = get(),
            client = get(),
            permissionsHelper = get(),
            transmitter = get(),
        )
    }
}
