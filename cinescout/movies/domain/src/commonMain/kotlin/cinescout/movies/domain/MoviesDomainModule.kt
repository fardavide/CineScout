package cinescout.movies.domain

import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetMovie
import cinescout.movies.domain.usecase.RateMovie
import org.koin.dsl.module

val MoviesDomainModule = module {

    factory { AddMovieToWatchlist(movieRepository = get()) }
    factory { GetAllRatedMovies(movieRepository = get()) }
    factory { GetMovie(movieRepository = get()) }
    factory { RateMovie(movieRepository = get()) }
}
