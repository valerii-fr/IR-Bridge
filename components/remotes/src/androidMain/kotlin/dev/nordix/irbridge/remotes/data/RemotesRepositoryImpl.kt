package dev.nordix.irbridge.remotes.data

import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.data.entity.RemoteDBView
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.domain.model.toDomain
import dev.nordix.irbridge.remotes.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RemotesRepositoryImpl(
    val dao: RemoteDao,
) : RemotesRepository {
    override suspend fun getAll(): List<Remote> {
        return dao.getAll().map(RemoteDBView::toDomain)
    }

    override suspend fun getById(id: ID<Remote>): Remote? {
        return dao.getById(id.value)?.toDomain()
    }

    override suspend fun save(vararg remote: Remote) {
        remote.forEach { r ->
            dao.save(r.toEntity())
            dao.save(*r.commands.map { c -> c.toEntity(r.id) }.toTypedArray())
        }
    }

    override fun observeAll(): Flow<List<Remote>> {
        return dao.observeAll().map { it.map(RemoteDBView::toDomain) }
    }

    override fun observeById(id: ID<Remote>) : Flow<Remote?> {
        return dao.observeById(id.value).map { it?.toDomain() }
    }

    override suspend fun delete(remoteId: ID<Remote>) {
        dao.deleteById(remoteId.value)
    }

    override suspend fun delete(remote: Remote) {
        delete(remote.id)
    }
}
