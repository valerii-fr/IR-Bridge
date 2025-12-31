package dev.nordix.irbridge.remotes.screens.add.model

import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.domain.model.Remote

data class RemoteAddState(
    val remote: Remote,
    val receivedPattern: IntArray,
    val bleReady: Boolean,
    val bleConnected: Boolean,
) {
    companion object {
        fun newDraft() = RemoteAddState(
            remote = Remote(
                id = ID.new(),
                name = "",
                description = null,
                commands = emptyList(),
            ),
            receivedPattern = IntArray(0),
            bleReady = false,
            bleConnected = false,
        )
    }
}
