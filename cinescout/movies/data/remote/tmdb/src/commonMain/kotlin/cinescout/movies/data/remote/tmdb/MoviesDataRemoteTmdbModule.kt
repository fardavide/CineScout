package cinescout.movies.data.remote.tmdb

import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val MoviesDataRemoteTmdbModule = module {

    factory<TmdbRemoteMovieDataSource> { RealTmdbMovieDataSource(client = get(TmdbNetworkQualifier.V3Client)) }
}
