package cinescout.media.data.local.mapper

import cinescout.database.model.DatabaseTmdbMovieVideo
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.media.domain.model.TmdbVideo
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseVideoMapper {

    fun toVideos(screenplayId: TmdbScreenplayId, videos: List<DatabaseTmdbMovieVideo>) = ScreenplayVideos(
        screenplayId = screenplayId,
        videos = videos.map(::toVideo)
    )

    fun toVideo(video: DatabaseTmdbMovieVideo) = TmdbVideo(
        id = video.id.toId(),
        key = video.key,
        resolution = video.resolution.toVideoResolution(),
        site = video.site.toVideoSite(),
        title = video.name,
        type = video.type.toVideoType()
    )
}
