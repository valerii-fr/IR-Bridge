package dev.nordix.irbridge.remotes.screens.list.model

import dev.nordix.irbridge.remotes.domain.model.Remote

data class RemotesListState(
    val remotes: List<Remote>
)
