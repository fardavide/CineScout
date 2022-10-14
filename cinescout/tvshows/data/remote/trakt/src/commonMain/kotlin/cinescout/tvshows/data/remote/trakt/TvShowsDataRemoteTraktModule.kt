package cinescout.tvshows.data.remote.trakt

import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.tvshows.data.remote.TraktRemoteTvShowDataSource
import cinescout.tvshows.data.remote.trakt.service.TraktTvShowService
import org.koin.dsl.module

val TvShowsDataRemoteTraktModule = module {

    factory { TraktTvShowService(client = get(TraktNetworkQualifier.Client)) }
    factory<TraktRemoteTvShowDataSource> { RealTraktTvShowDataSource(service = get()) }
}
