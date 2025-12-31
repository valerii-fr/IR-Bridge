package dev.nordix.irbridge.remotes.di

import androidx.room.Room
import dev.nordix.irbridge.remotes.data.RemoteDao
import dev.nordix.irbridge.remotes.data.RemoteDatabase
import dev.nordix.irbridge.remotes.data.RemotesRepositoryImpl
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import org.koin.dsl.module

val remotesModule = module {
    single<RemoteDatabase> {
        Room.databaseBuilder(
            get(),
            RemoteDatabase::class.java,
            "remotes.db"
        )
            .build()
    }

    single<RemoteDao> {
        get<RemoteDatabase>().remoteDao
    }

    single<RemotesRepository> {
        RemotesRepositoryImpl(dao = get())
    }
}
