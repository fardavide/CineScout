package cinescout.media.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemoteMediaDataSource {

    suspend fun getImages(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayImages>

    suspend fun getVideos(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayVideos>
}
