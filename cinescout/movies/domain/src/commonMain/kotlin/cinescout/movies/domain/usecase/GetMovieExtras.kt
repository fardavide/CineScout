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

class GetMovieExtras(
    private val getMovieCredits: GetMovieCredits,
    private val getMovieDetails: GetMovieDetails
) {

    operator fun invoke(id: TmdbMovieId): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getMovieCredits(id),
            getMovieDetails(id)
        ) { creditsEither, detailsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    personalRating = none() // TODO()
                )
            }
        }

    operator fun invoke(movieWithPersonalRating: MovieWithPersonalRating): Flow<Either<DataError, MovieWithExtras>> =
        combine(
            getMovieCredits(movieWithPersonalRating.movie.tmdbId),
            getMovieDetails(movieWithPersonalRating.movie.tmdbId)
        ) { creditsEither, detailsEither ->
            either {
                MovieWithExtras(
                    movieWithDetails = detailsEither.bind(),
                    credits = creditsEither.bind(),
                    personalRating = movieWithPersonalRating.rating.some()
                )
            }
        }

    operator fun invoke(movie: Movie): Flow<Either<DataError, MovieWithExtras>> =
        this(movie.tmdbId)
}
