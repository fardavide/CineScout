package cinescout.media.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.media.data.datasource.RemoteMediaDataSource
import cinescout.media.data.remote.mapper.TmdbScreenplayImagesMapper
import cinescout.media.data.remote.mapper.TmdbScreenplayVideosMapper
import cinescout.media.data.remote.service.TmdbMediaService
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteMediaDataSource(
    private val imagesMapper: TmdbScreenplayImagesMapper,
    private val service: TmdbMediaService,
    private val videosMapper: TmdbScreenplayVideosMapper
) : RemoteMediaDataSource {

    override suspend fun getImages(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayImages> =
        service.getImages(screenplayId).map(imagesMapper::toScreenplayImages)

    override suspend fun getVideos(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayVideos> =
        service.getVideos(screenplayId).map(videosMapper::toScreenplayVideos)
}
