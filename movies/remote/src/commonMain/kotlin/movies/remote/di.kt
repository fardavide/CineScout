package movies.remote

import movies.RemoteMovieSource
import movies.moviesModule
import org.koin.dsl.module

val remoteMoviesModule = module {
    factory<RemoteMovieSource> {
        RemoteMovieSourceImpl(
            tmdbSource = get()
        )
    }
} + moviesModule
