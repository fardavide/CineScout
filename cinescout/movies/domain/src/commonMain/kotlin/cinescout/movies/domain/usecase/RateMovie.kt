package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class RateMovie(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movie: Movie, rating: Rating): Either<DataError, Unit> =
        movieRepository.rate(movie, rating)
}
