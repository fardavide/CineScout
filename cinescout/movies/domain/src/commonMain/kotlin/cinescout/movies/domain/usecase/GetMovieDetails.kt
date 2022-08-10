package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.Refresh
import kotlinx.coroutines.flow.Flow

class GetMovieDetails(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(id: TmdbMovieId, refresh: Refresh = Refresh.Once): Flow<Either<DataError, MovieWithDetails>> =
        movieRepository.getMovieDetails(id, refresh)
}
