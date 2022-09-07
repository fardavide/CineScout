package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Refresh
import kotlin.time.Duration.Companion.minutes

class GetMoviePersonalRating(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(validity = 5.minutes)
    ): Flow<Either<DataError, Option<Rating>>> =
        movieRepository.getAllRatedMovies(refresh).map { either ->
            either.map { movies ->
                movies.data.find { it.movie.tmdbId == id }?.personalRating?.some() ?: none()
            }
        }
}
