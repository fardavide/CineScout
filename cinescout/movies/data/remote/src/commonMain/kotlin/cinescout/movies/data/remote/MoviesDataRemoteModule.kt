package cinescout.movies.data.remote

import cinescout.movies.data.RemoteMovieDataSource
import org.koin.dsl.module

val MoviesDataRemoteModule = module {

    factory<RemoteMovieDataSource> {
        RealRemoteMovieDataSource(
            dualSourceCall = get(),
            isTmdbLinked = get(),
            isTraktLinked = get(),
            tmdbSource = get(),
            traktSource = get()
        )
    }
}
