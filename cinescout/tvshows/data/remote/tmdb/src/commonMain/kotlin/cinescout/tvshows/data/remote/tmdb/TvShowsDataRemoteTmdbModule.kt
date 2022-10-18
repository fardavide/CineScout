package cinescout.tvshows.data.remote.tmdb

import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.tvshows.data.remote.TmdbRemoteTvShowDataSource
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowCreditsMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowImagesMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowKeywordMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowVideosMapper
import cinescout.tvshows.data.remote.tmdb.service.TmdbTvShowService
import org.koin.dsl.module

val TvShowsDataRemoteTmdbModule = module {

    factory { TmdbTvShowCreditsMapper() }
    factory { TmdbTvShowImagesMapper() }
    factory { TmdbTvShowKeywordMapper() }
    factory { TmdbTvShowMapper() }
    factory { TmdbTvShowService(authProvider = get(), v3client = get(TmdbNetworkQualifier.V3.Client)) }
    factory { TmdbTvShowVideosMapper() }
    factory<TmdbRemoteTvShowDataSource> {
        RealTmdbTvShowDataSource(
            callWithTmdbAccount = get(),
            tvShowCreditsMapper = get(),
            tvShowImagesMapper = get(),
            tvShowKeywordMapper = get(),
            tvShowMapper = get(),
            tvShowService = get(),
            tvShowVideosMapper = get()
        )
    }
}
