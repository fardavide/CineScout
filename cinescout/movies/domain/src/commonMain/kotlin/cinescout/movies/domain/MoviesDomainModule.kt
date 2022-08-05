package cinescout.movies.domain

import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetAllKnownMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetMovie
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.movies.domain.usecase.RateMovie
import cinescout.movies.domain.usecase.SyncRatedMovies
import org.koin.dsl.module

val MoviesDomainModule = module {

    factory { AddMovieToWatchlist(movieRepository = get()) }
    factory { GetAllRatedMovies(movieRepository = get()) }
    factory { GetAllKnownMovies(getAllRatedMovies = get()) }
    factory { GetMovie(movieRepository = get()) }
    factory { GetMovieCredits(movieRepository = get()) }
    factory { RateMovie(movieRepository = get()) }
    factory { SyncRatedMovies(movieRepository = get()) }
}
