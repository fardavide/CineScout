package movies.remote.tmdb

import movies.remote.TmdbRemoteMovieSource
import movies.remote.remoteMoviesModule
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieSearchService
import movies.remote.tmdb.movie.MovieService
import network.tmdb.tmdbNetworkModule
import network.tmdb.v3Client
import org.koin.dsl.module

val tmdbRemoteMoviesModule = module {

    factory<TmdbRemoteMovieSource> {
        TmdbRemoteMovieSourceImpl(
            movieDiscoverService = get(),
            movieService = get(),
            movieSearchService = get()
        )
    }

    factory { MovieDiscoverService(client = get(v3Client)) }
    factory { MovieService(client = get(v3Client)) }
    factory { MovieSearchService(client = get(v3Client)) }

} + remoteMoviesModule + tmdbNetworkModule
