package dev.nordix.irbridge.feature.widget.di

import dev.nordix.irbridge.feature.widget.domain.model.WidgetControlViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val widgetModule = module {
    viewModel { (remoteId: String) ->
        WidgetControlViewModel(
            remoteId = remoteId,
            remotesRepository = get(),
            transmitter = get()
        )
    }
}