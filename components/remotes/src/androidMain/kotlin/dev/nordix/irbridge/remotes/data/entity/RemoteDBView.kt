package dev.nordix.irbridge.remotes.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RemoteDBView(
    @Embedded
    val remote: RemoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "remoteId",
    )
    val commands: List<RemoteCommandEntity>,
)
