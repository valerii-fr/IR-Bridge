package dev.nordix.irbridge.remotes.domain

import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.domain.model.Remote
import kotlinx.coroutines.flow.Flow

interface RemotesRepository {
    suspend fun getAll() : List<Remote>
    suspend fun getById(id: ID<Remote>) : Remote?
    suspend fun save(vararg remote: Remote)

    fun observeAll() : Flow<List<Remote>>
    fun observeById(id: ID<Remote>) : Flow<Remote?>

    suspend fun delete(remoteId: ID<Remote>)
    suspend fun delete(remote: Remote)
}
