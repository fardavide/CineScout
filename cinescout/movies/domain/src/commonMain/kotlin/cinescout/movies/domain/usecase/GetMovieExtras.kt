package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.some
import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory

@Factory
class GetMovieExtras(
    private val getIsMovieInWatchlist: GetIsMovieInWatchlist,
    private val getMovieCredits: GetMovieCredits,
    private val getMovieDetails: GetMovieDetails,
    private val getMovieKeywords: GetMovieKeywords,
    private val getMoviePersonalRating: GetMoviePersonalRating
) {

    operator fun invoke(
        movieId: TmdbMovieId,
        refresh: Boolean = true
    ): Flow<Either<NetworkError, MovieWithExtras>> = combine(
        getIsMovieInWatchlist(movieId, refresh),
        getMovieCredits(movieId, refresh).filterData(),
        getMovieDetails(movieId, refresh).filterData(),
        getMovieKeywords(movieId, refresh).filterData(),
        getMoviePersonalRating(movieId, refresh)
    ) { isInWatchlistEither, creditsEither, detailsEither, keywordsEither, personalRatingEither ->
        either {
            MovieWithExtras(
                movieWithDetails = detailsEither.bind(),
                isInWatchlist = isInWatchlistEither.bind(),
                credits = creditsEither.bind(),
                keywords = keywordsEither.bind(),
                personalRating = personalRatingEither.bind()
            )
        }
    }

    operator fun invoke(movie: Movie, refresh: Boolean = true): Flow<Either<NetworkError, MovieWithExtras>> =
        this(movie.tmdbId, refresh)

    operator fun invoke(
        movieWithPersonalRating: MovieWithPersonalRating,
        refresh: Boolean = false
    ): Flow<Either<NetworkError, MovieWithExtras>> = combine(
        getIsMovieInWatchlist(movieWithPersonalRating.movie.tmdbId, refresh),
        getMovieCredits(movieWithPersonalRating.movie.tmdbId, refresh).filterData(),
        getMovieDetails(movieWithPersonalRating.movie.tmdbId, refresh).filterData(),
        getMovieKeywords(movieWithPersonalRating.movie.tmdbId, refresh).filterData()
    ) { isInWatchlistEither, creditsEither, detailsEither, keywordsEither ->
        either {
            MovieWithExtras(
                movieWithDetails = detailsEither.bind(),
                isInWatchlist = isInWatchlistEither.bind(),
                credits = creditsEither.bind(),
                keywords = keywordsEither.bind(),
                personalRating = movieWithPersonalRating.personalRating.some()
            )
        }
    }
}
