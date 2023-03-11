package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.NetworkError
import cinescout.store5.ext.filterData
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowMedia
import cinescout.tvshows.domain.store.TvShowImagesStore
import cinescout.tvshows.domain.store.TvShowImagesStoreKey
import cinescout.tvshows.domain.store.TvShowVideosStore
import cinescout.tvshows.domain.store.TvShowVideosStoreKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetTvShowMedia(
    private val tvShowImagesStore: TvShowImagesStore,
    private val tvShowVideosStore: TvShowVideosStore
) {

    operator fun invoke(tvShowId: TmdbTvShowId, refresh: Boolean): Flow<Either<NetworkError, TvShowMedia>> =
        combine(
            images(tvShowId, refresh),
            videos(tvShowId, refresh)
        ) { imagesEither, videosEither ->
            either {
                TvShowMedia(
                    backdrops = imagesEither.bind().backdrops,
                    posters = imagesEither.bind().posters,
                    videos = videosEither.bind().videos
                )
            }
        }
    
    private fun images(tvShowId: TmdbTvShowId, refresh: Boolean) =
        tvShowImagesStore.stream(StoreReadRequest.cached(TvShowImagesStoreKey(tvShowId), refresh)).filterData()
    
    private fun videos(tvShowId: TmdbTvShowId, refresh: Boolean) =
        tvShowVideosStore.stream(StoreReadRequest.cached(TvShowVideosStoreKey(tvShowId), refresh)).filterData()
}
