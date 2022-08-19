package cinescout.movies.data

import cinescout.movies.domain.MovieRepository
import org.koin.dsl.module

val MoviesDataModule = module {

    factory<MovieRepository> {
        RealMovieRepository(
            localMovieDataSource = get(),
            remoteMovieDataSource = get(),
            storeOwner = get()
        )
    }
}
