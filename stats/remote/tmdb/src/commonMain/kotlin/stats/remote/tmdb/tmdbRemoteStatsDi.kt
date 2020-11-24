package stats.remote.tmdb

import network.tmdb.v3accountId
import network.tmdb.v4Client
import network.tmdb.v4accountId
import org.koin.dsl.module
import stats.remote.TmdbRemoteStatSource
import stats.remote.remoteStatsModule
import stats.statsModule

val tmdbRemoteStatsModule = module {

    factory<TmdbRemoteStatSource> { TmdbRemoteStatsSourceImpl(accountService = get(), movieResultMapper = get()) }
    factory { AccountService(client = get(v4Client), v3AccountId = get(v3accountId), v4accountId = get(v4accountId)) }

} + statsModule + remoteStatsModule
