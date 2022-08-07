package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

class RateMovie(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> =
        movieRepository.rate(movieId, rating)
}
