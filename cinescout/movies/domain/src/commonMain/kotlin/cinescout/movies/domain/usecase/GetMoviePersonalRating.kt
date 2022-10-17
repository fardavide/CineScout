package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.common.model.Rating
import cinescout.error.DataError
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Refresh
import kotlin.time.Duration.Companion.minutes

class GetMoviePersonalRating(
    private val getAllRatedMovies: GetAllRatedMovies
) {

    operator fun invoke(
        movieId: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(validity = 5.minutes)
    ): Flow<Either<DataError, Option<Rating>>> =
        getAllRatedMovies(refresh).loadAll().map { either ->
            either.map { movies ->
                movies.data.find { it.movie.tmdbId == movieId }?.personalRating?.some() ?: none()
            }
        }
}
