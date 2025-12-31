package dev.nordix.irbridge.remotes.domain.model

import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.core.utils.Identifiable
import kotlinx.serialization.Serializable

@Serializable
data class Remote(
    override val id: ID<Remote>,
    val name: String,
    val description: String?,
    val commands: List<RemoteCommand>,
) : Identifiable<Remote>
