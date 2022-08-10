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
import cinescout.store.Refresh
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMovieExtras(
    private val getMovieCredits: GetMovieCredits,
    private val getMovieDetails: GetMovieDetails
) {

    operator fun invoke(id: TmdbMovieId, refresh: Refresh = Refresh.Once): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getMovieCredits(id, refresh),
            getMovieDetails(id, refresh)
        ) { creditsEither, detailsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    personalRating = none() // TODO Get movie personal rating
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
            getMovieDetails(movieWithPersonalRating.movie.tmdbId, refresh)
        ) { creditsEither, detailsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    personalRating = movieWithPersonalRating.rating.some()
                )
            }
        }
}
