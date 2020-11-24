package stats.remote.trakt

import network.trakt.client
import org.koin.dsl.module
import stats.remote.TraktRemoteStatSource
import stats.remote.remoteStatsModule
import stats.statsModule

val traktRemoteStatsModule = module {

    factory<TraktRemoteStatSource> { TraktRemoteStatSourceImpl(syncService = get(), findMovie = get()) }
    factory { SyncService(client = get(client)) }

} + statsModule + remoteStatsModule
