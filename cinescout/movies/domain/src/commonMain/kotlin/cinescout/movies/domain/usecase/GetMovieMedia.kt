package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieMedia
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import store.Refresh
import kotlin.time.Duration.Companion.days

class GetMovieMedia(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        id: TmdbMovieId,
        refresh: Refresh = Refresh.IfExpired(7.days)
    ): Flow<Either<DataError, MovieMedia>> =
        combine(
            movieRepository.getMovieImages(id, refresh),
            movieRepository.getMovieVideos(id, refresh)
        ) { imagesEither, videosEither ->
            either {
                MovieMedia(
                    backdrops = imagesEither.bind().backdrops,
                    posters = imagesEither.bind().posters,
                    videos = videosEither.bind().videos
                )
            }
        }
}
