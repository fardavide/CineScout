package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import store.Refresh
import kotlin.time.Duration.Companion.days

@Factory
class GetTvShowMedia(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        id: TmdbTvShowId,
        refresh: Refresh = Refresh.IfExpired(7.days)
    ): Flow<Either<DataError, TvShowMedia>> =
        combine(
            tvShowRepository.getTvShowImages(id, refresh),
            tvShowRepository.getTvShowVideos(id, refresh)
        ) { imagesEither, videosEither ->
            either {
                TvShowMedia(
                    backdrops = imagesEither.bind().backdrops,
                    posters = imagesEither.bind().posters,
                    videos = videosEither.bind().videos
                )
            }
        }
}
