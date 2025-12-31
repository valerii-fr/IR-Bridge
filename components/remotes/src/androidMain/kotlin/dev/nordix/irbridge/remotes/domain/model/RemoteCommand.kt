package dev.nordix.irbridge.remotes.domain.model

import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.core.utils.Identifiable
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCommand(
    override val id: ID<RemoteCommand>,
    val name: String,
    val description: String?,
    val icon: Int?,
    val color: Int?,
    val durations: IntArray,
) : Identifiable<RemoteCommand>
