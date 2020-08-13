package movies

import entities.movies.MovieRepository
import org.koin.dsl.module

val moviesModule = module {
    factory<MovieRepository> {
        MovieRepositoryImpl(
            remoteSource = get()
        )
    }
}
