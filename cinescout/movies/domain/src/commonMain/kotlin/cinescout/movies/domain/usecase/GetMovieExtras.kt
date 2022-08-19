package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.none
import arrow.core.some
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import store.Refresh

class GetMovieExtras(
    private val getMovieCredits: GetMovieCredits,
    private val getMovieDetails: GetMovieDetails,
    private val getMovieKeywords: GetMovieKeywords
) {

    operator fun invoke(id: TmdbMovieId, refresh: Refresh = Refresh.Once): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getMovieCredits(id, refresh),
            getMovieDetails(id, refresh),
            getMovieKeywords(id, refresh)
        ) { creditsEither, detailsEither, keywordsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    keywords = keywordsEither.bind(),
                    personalRating = none()
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
            getMovieCredits(movieWithPersonalRating.movie.tmdbId, refresh),
            getMovieDetails(movieWithPersonalRating.movie.tmdbId, refresh),
            getMovieKeywords(movieWithPersonalRating.movie.tmdbId, refresh)
        ) { creditsEither, detailsEither, keywordsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    keywords = keywordsEither.bind(),
                    personalRating = movieWithPersonalRating.rating.some()
                )
            }
        }
}
