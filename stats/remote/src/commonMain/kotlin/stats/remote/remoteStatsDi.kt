package stats.remote

import network.tmdb.accountId
import network.tmdb.v4Client
import org.koin.dsl.module
import stats.RemoteStatSource
import stats.statsModule

val remoteStatsModule = module {

    factory<RemoteStatSource> { RemoteStatsSourceImpl(accountService = get(), moviePageResultMapper = get()) }

    factory { AccountService(client = get(v4Client), accountId = get(accountId)) }

} + statsModule
