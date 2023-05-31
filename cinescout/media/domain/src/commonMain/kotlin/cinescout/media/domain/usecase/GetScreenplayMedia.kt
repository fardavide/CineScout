package cinescout.media.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.store.ScreenplayImagesStore
import cinescout.media.domain.store.ScreenplayVideosStore
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.store5.ext.filterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayMedia {

    operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayMedia>>
}

@Factory
internal class RealGetScreenplayMedia(
    private val screenplayImagesStore: ScreenplayImagesStore,
    private val screenplayVideosStore: ScreenplayVideosStore
) : GetScreenplayMedia {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayMedia>> = combine(
        images(screenplayId, refresh),
        videos(screenplayId, refresh)
    ) { imagesEither, videosEither ->
        either {
            ScreenplayMedia.from(
                screenplayId = screenplayId,
                backdrops = imagesEither.bind().backdrops,
                posters = imagesEither.bind().posters,
                videos = videosEither.bind().videos
            )
        }
    }

    private fun images(screenplayId: TmdbScreenplayId, refresh: Boolean) =
        screenplayImagesStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()

    private fun videos(screenplayId: TmdbScreenplayId, refresh: Boolean) =
        screenplayVideosStore.stream(StoreReadRequest.cached(screenplayId, refresh)).filterData()
}

class FakeGetScreenplayMedia(
    private val screenplayMedia: ScreenplayMedia? = null
) : GetScreenplayMedia {

    override operator fun invoke(
        screenplayId: TmdbScreenplayId,
        refresh: Boolean
    ): Flow<Either<NetworkError, ScreenplayMedia>> =
        flowOf(screenplayMedia?.right() ?: NetworkError.Unknown.left())
}
