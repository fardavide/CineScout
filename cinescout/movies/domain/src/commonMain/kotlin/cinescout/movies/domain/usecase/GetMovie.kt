package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

class GetMovie(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(id: TmdbMovieId): Flow<Either<DataError, Movie>> =
        movieRepository.getMovie(id)
}
