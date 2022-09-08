package cinescout.movies.domain

import cinescout.movies.domain.usecase.AddMovieToDislikedList
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.movies.domain.usecase.AddMovieToWatchlist
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.movies.domain.usecase.GetIsMovieInWatchlist
import cinescout.movies.domain.usecase.GetMovieCredits
import cinescout.movies.domain.usecase.GetMovieDetails
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.movies.domain.usecase.GetMovieKeywords
import cinescout.movies.domain.usecase.GetMoviePersonalRating
import cinescout.movies.domain.usecase.RateMovie
import cinescout.movies.domain.usecase.RemoveMovieFromWatchlist
import org.koin.dsl.module

val MoviesDomainModule = module {

    factory { AddMovieToDislikedList(movieRepository = get()) }
    factory { AddMovieToLikedList(movieRepository = get()) }
    factory { AddMovieToWatchlist(movieRepository = get()) }
    factory { GetAllDislikedMovies(movieRepository = get()) }
    factory { GetIsMovieInWatchlist(getAllWatchlistMovies = get()) }
    factory { GetMovieKeywords(movieRepository = get()) }
    factory { GetMoviePersonalRating(getAllRatedMovies = get()) }
    factory { GetAllLikedMovies(movieRepository = get()) }
    factory { GetAllRatedMovies(movieRepository = get()) }
    factory { GetAllWatchlistMovies(movieRepository = get()) }
    factory { GetMovieDetails(movieRepository = get()) }
    factory { GetMovieCredits(movieRepository = get()) }
    factory {
        GetMovieExtras(
            getIsMovieInWatchlist = get(),
            getMovieCredits = get(),
            getMovieDetails = get(),
            getMovieKeywords = get(),
            getMoviePersonalRating = get()
        )
    }
    factory { RateMovie(movieRepository = get()) }
    factory { RemoveMovieFromWatchlist(movieRepository = get()) }
}
