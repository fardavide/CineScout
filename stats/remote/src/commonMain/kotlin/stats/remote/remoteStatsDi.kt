package stats.remote

import network.tmdb.v3accountId
import network.tmdb.v4Client
import network.tmdb.v4accountId
import org.koin.dsl.module
import stats.RemoteStatSource
import stats.statsModule

val remoteStatsModule = module {

    factory<RemoteStatSource> { RemoteStatsSourceImpl(accountService = get(), movieResultMapper = get()) }

} + statsModule
