package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.some
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import store.Refresh

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
        refresh: Refresh = Refresh.IfExpired()
    ): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getIsMovieInWatchlist(movieId, refresh),
            getMovieCredits(movieId, refresh),
            getMovieDetails(movieId, refresh),
            getMovieKeywords(movieId, refresh),
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

    operator fun invoke(movie: Movie, refresh: Refresh = Refresh.Once): Flow<Either<DataError, MovieWithExtras>> =
        this(movie.tmdbId, refresh)

    operator fun invoke(
        movieWithPersonalRating: MovieWithPersonalRating,
        refresh: Refresh = Refresh.Once
    ): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getIsMovieInWatchlist(movieWithPersonalRating.movie.tmdbId, refresh),
            getMovieCredits(movieWithPersonalRating.movie.tmdbId, refresh),
            getMovieDetails(movieWithPersonalRating.movie.tmdbId, refresh),
            getMovieKeywords(movieWithPersonalRating.movie.tmdbId, refresh)
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
