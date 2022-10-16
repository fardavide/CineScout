package cinescout.tvshows.data.remote.tmdb

import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.tvshows.data.remote.TmdbRemoteTvShowDataSource
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowMapper
import cinescout.tvshows.data.remote.tmdb.service.TmdbTvShowService
import org.koin.dsl.module

val TvShowsDataRemoteTmdbModule = module {

    factory { TmdbTvShowMapper() }
    factory { TmdbTvShowService(authProvider = get(), v3client = get(TmdbNetworkQualifier.V3.Client)) }
    factory<TmdbRemoteTvShowDataSource> {
        RealTmdbTvShowDataSource(
            callWithTmdbAccount = get(),
            tvShowMapper = get(),
            tvShowService = get()
        )
    }
}
