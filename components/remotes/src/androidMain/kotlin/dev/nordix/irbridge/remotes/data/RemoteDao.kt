package dev.nordix.irbridge.remotes.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.nordix.irbridge.remotes.data.entity.RemoteCommandEntity
import dev.nordix.irbridge.remotes.data.entity.RemoteDBView
import dev.nordix.irbridge.remotes.data.entity.RemoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RemoteDao {

    @Query("SELECT * FROM remotes")
    suspend fun getAll(): List<RemoteDBView>

    @Query("SELECT * FROM remotes WHERE id = :id")
    suspend fun getById(id: String): RemoteDBView?

    @Query("SELECT * FROM remotes")
    fun observeAll(): Flow<List<RemoteDBView>>

    @Query("SELECT * FROM remotes WHERE id = :id")
    fun observeById(id: String): Flow<RemoteDBView?>

    @Query("DELETE FROM remotes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM remote_commands WHERE remoteId = :id")
    suspend fun deleteCommandsByRemoteId(id: String)

    @Upsert
    suspend fun save(vararg remote: RemoteEntity)

    @Upsert
    suspend fun save(vararg remote: RemoteCommandEntity)

}
