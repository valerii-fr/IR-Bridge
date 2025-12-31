package dev.nordix.irbridge.remotes.screens.list.model

import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand

sealed interface RemotesListEvent {
    data class OnRemoteDeleteClicked(val remote: Remote) : RemotesListEvent

    data class OnRemoteCommandDeleteClicked(val remote: Remote, val command: RemoteCommand) : RemotesListEvent
    data class OnRemoteCommandSendClicked(val remote: Remote, val command: RemoteCommand) : RemotesListEvent
}
