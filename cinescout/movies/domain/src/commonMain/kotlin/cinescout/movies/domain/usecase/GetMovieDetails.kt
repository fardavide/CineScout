package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import store.Refresh

class GetMovieDetails(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(id: TmdbMovieId, refresh: Refresh = Refresh.Once): Flow<Either<DataError, MovieWithDetails>> =
        movieRepository.getMovieDetails(id, refresh)
}
