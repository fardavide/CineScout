package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Refresh
import kotlin.time.Duration.Companion.days

@Factory
class GetMovieDetails(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        movieId: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(7.days)
    ): Flow<Either<DataError, MovieWithDetails>> =
        movieRepository.getMovieDetails(movieId, refresh)
}
