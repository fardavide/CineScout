package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import store.Refresh
import kotlin.time.Duration.Companion.days

class GetMovieMedia(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(7.days)
    ): Flow<Either<DataError, MovieMedia>> =
        movieRepository.getMovieImages(id, refresh).map { either ->
            either.map { movieImages ->
                MovieMedia(
                    backdrops = movieImages.backdrops,
                    posters = movieImages.posters
                )
            }
        }
}
