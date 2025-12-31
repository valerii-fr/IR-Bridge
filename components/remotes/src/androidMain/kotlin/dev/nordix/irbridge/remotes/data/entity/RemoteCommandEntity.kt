package dev.nordix.irbridge.remotes.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "remote_commands",
    primaryKeys = ["id", "remoteId"],
    foreignKeys = [
        ForeignKey(
            entity = RemoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["remoteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id"), Index("remoteId")],
)
data class RemoteCommandEntity(
    val id: String,
    val remoteId: String,
    val name: String,
    val description: String?,
    val icon: Int?,
    val color: Int?,
    val durations: String,
)
