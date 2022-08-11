package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.Refresh
import kotlinx.coroutines.flow.Flow

class GetMovieKeywords(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.Once
    ): Flow<Either<DataError, MovieKeywords>> =
        movieRepository.getMovieKeywords(id, refresh)
}
