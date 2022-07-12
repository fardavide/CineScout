package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithRating
import kotlinx.coroutines.flow.Flow

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<DataError, List<MovieWithRating>>> =
        movieRepository.getAllRatedMovies()
}
