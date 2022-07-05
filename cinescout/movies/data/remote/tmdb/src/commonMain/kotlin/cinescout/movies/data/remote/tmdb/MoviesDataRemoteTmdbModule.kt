package cinescout.movies.data.remote.tmdb

import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val MoviesDataRemoteTmdbModule = module {

    factory { TmdbMovieMapper() }
    factory { TmdbMovieService(client = get(TmdbNetworkQualifier.V3.Client)) }
    factory<TmdbRemoteMovieDataSource> { RealTmdbMovieDataSource(movieMapper = get(), service = get()) }
}
