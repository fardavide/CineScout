package cinescout.movies.data.remote.tmdb

import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieImagesMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieKeywordMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieVideosMapper
import cinescout.movies.data.remote.tmdb.service.TmdbMovieSearchService
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val MoviesDataRemoteTmdbModule = module {

    factory { TmdbMovieCreditsMapper() }
    factory { TmdbMovieKeywordMapper() }
    factory { TmdbMovieImagesMapper() }
    factory { TmdbMovieMapper() }
    factory { TmdbMovieService(authProvider = get(), v3client = get(TmdbNetworkQualifier.V3.Client)) }
    factory { TmdbMovieVideosMapper() }
    factory<TmdbRemoteMovieDataSource> {
        RealTmdbMovieDataSource(
            callWithTmdbAccount = get(),
            movieCreditsMapper = get(),
            movieKeywordMapper = get(),
            movieImagesMapper = get(),
            movieMapper = get(),
            movieVideosMapper = get(),
            movieService = get(),
            searchService = get()
        )
    }
    factory { TmdbMovieSearchService(authProvider = get(), client = get(TmdbNetworkQualifier.V3.Client)) }
}
