package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

class GetAllDislikedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<DataError.Local, List<Movie>>> =
        movieRepository.getAllDislikedMovies()
}
