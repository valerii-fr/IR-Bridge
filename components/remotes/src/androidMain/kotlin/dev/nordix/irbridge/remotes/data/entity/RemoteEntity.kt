package dev.nordix.irbridge.remotes.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.core.utils.Identifiable
import java.util.UUID

@Entity(
    tableName = "remotes",
    primaryKeys = ["id"],
    indices = [Index("id")],
)
data class RemoteEntity(
    val id: String,
    val name: String,
    val description: String?,
)
