package cinescout.media.data.local.mapper

import cinescout.database.model.DatabaseScreenplayVideo
import cinescout.database.model.DatabaseTmdbVideoId
import cinescout.database.model.DatabaseVideoResolution
import cinescout.database.model.DatabaseVideoSite
import cinescout.database.model.DatabaseVideoType
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.media.domain.model.TmdbVideo
import cinescout.screenplay.data.local.mapper.toDatabaseId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseVideoMapper {

    fun toVideo(
        @Suppress("UNUSED_PARAMETER") screenplayId: DatabaseTmdbScreenplayId,
        id: DatabaseTmdbVideoId,
        key: String,
        name: String,
        resolution: DatabaseVideoResolution,
        site: DatabaseVideoSite,
        type: DatabaseVideoType
    ) = TmdbVideo(
        id = id.toId(),
        key = key,
        resolution = resolution.toVideoResolution(),
        site = site.toVideoSite(),
        title = name,
        type = type.toVideoType()
    )

    fun toDatabaseVideos(screenplayVideos: ScreenplayVideos): List<DatabaseScreenplayVideo> {
        return screenplayVideos.videos.map { video ->
            DatabaseScreenplayVideo(
                id = video.id.toDatabaseId(),
                screenplayId = screenplayVideos.screenplayId.toDatabaseId(),
                key = video.key,
                name = video.title,
                resolution = video.resolution.toDatabaseVideoResolution(),
                site = video.site.toDatabaseVideoSite(),
                type = video.type.toDatabaseVideoType()
            )
        }
    }
}
