package dev.nordix.irbridge.remotes.screens.add.model

import dev.nordix.irbridge.remotes.domain.model.RemoteCommand

sealed interface RemoteAddEvent {
    data class UpdateRemote(
        val name: String,
        val description: String,
    ) : RemoteAddEvent

    data class UpdateRemoteCommands(
        val commands: List<RemoteCommand>
    ) : RemoteAddEvent

    data object SaveRemote : RemoteAddEvent
    data object ClearReceived : RemoteAddEvent
    data class SendPattern(val durations: IntArray) : RemoteAddEvent
}
