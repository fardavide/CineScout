package cinescout.movies.domain

import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.movies.domain.usecase.GetMovieDetails
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.movies.domain.usecase.GetMovieKeywords
import cinescout.movies.domain.usecase.RateMovie
import org.koin.dsl.module

val MoviesDomainModule = module {

    factory { AddMovieToDislikedList(movieRepository = get()) }
    factory { AddMovieToLikedList(movieRepository = get()) }
    factory { AddMovieToWatchlist(movieRepository = get()) }
    factory { GetAllDislikedMovies(movieRepository = get()) }
    factory { GetMovieKeywords(movieRepository = get()) }
    factory { GetAllLikedMovies(movieRepository = get()) }
    factory { GetAllRatedMovies(movieRepository = get()) }
    factory { GetAllWatchlistMovies(movieRepository = get()) }
    factory { GetMovieDetails(movieRepository = get()) }
    factory { GetMovieCredits(movieRepository = get()) }
    factory { GetMovieExtras(getMovieCredits = get(), getMovieDetails = get(), getMovieKeywords = get()) }
    factory { RateMovie(movieRepository = get()) }
}
