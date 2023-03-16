package cinescout.media.data.datasource

import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface LocalMediaDataSource {

    fun findImages(screenplayId: TmdbScreenplayId): Flow<ScreenplayImages?>

    fun findVideos(screenplayId: TmdbScreenplayId): Flow<ScreenplayVideos?>

    suspend fun insertImages(images: ScreenplayImages)

    suspend fun insertVideos(videos: ScreenplayVideos)
}
