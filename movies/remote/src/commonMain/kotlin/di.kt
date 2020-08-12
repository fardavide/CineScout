import org.koin.dsl.module

val remoteMoviesModule = module {
    factory<RemoteMovieSouce> {
        RemoteMovieSourceImpl(
            tmdbSource = get()
        )
    }
} + moviesModule
