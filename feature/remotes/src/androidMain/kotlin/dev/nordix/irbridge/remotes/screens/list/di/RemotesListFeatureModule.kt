package dev.nordix.irbridge.remotes.screens.list.di

import dev.nordix.irbridge.remotes.screens.list.model.RemotesListScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val remotesListFeatureModule = module {
    viewModel {
        RemotesListScreenViewModel(
            remotesRepository = get(),
            transmitter = get()
        )
    }
}
