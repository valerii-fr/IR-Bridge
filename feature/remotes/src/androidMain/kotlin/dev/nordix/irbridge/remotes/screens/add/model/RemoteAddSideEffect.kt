package dev.nordix.irbridge.remotes.screens.add.model

sealed interface RemoteAddSideEffect {
    data object RemoteSaved : RemoteAddSideEffect
}
