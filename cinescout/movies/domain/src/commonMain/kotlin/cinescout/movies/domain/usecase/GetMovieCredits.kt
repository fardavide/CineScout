package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

class GetMovieCredits(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(id: TmdbMovieId): Flow<Either<DataError.Remote, MovieCredits>> =
        movieRepository.getMovieCredits(id)
}
