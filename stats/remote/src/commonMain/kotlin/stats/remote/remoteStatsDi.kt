package stats.remote

import org.koin.dsl.module
import stats.RemoteStatSource
import stats.statsModule

val remoteStatsModule = module {

    factory<RemoteStatSource> { RemoteStatsSourceImpl(tmdbSource = get(), traktSource = get()) }

} + statsModule
