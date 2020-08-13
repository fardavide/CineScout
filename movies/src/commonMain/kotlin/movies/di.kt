package movies

import org.koin.dsl.module

val moviesModule = module {
    factory<MovieRepository> {
        MovieRepositoryImpl(
            removeSource = get()
        )
    }
}
