package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetMovieCredits(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.IfNeeded
    ): Flow<Either<DataError, MovieCredits>> =
        movieRepository.getMovieCredits(id, refresh)
}
