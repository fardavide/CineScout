package cinescout.media.data.local.mapper

import cinescout.database.model.DatabaseScreenplayBackdrop
import cinescout.database.model.DatabaseScreenplayPoster
import cinescout.database.model.DatabaseScreenplayVideo
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.screenplay.data.local.mapper.toDatabaseId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseImageMapper {

    fun toBackdrop(
        @Suppress("UNUSED_PARAMETER") screenplayId: DatabaseTmdbScreenplayId,
        isPrimary: Boolean,
        path: String
    ) = TmdbBackdropImage(isPrimary = isPrimary, path = path)

    fun toPoster(
        @Suppress("UNUSED_PARAMETER") screenplayId: DatabaseTmdbScreenplayId,
        isPrimary: Boolean,
        path: String
    ) = TmdbPosterImage(isPrimary = isPrimary, path = path)

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

    fun toDatabaseBackdrops(screenplayImages: ScreenplayImages): List<DatabaseScreenplayBackdrop> {
        return screenplayImages.backdrops.map { backdrop ->
            DatabaseScreenplayBackdrop(
                screenplayId = screenplayImages.screenplayId.toDatabaseId(),
                isPrimary = backdrop.isPrimary,
                path = backdrop.path
            )
        }
    }

    fun toDatabasePosters(screenplayImages: ScreenplayImages): List<DatabaseScreenplayPoster> {
        return screenplayImages.posters.map { poster ->
            DatabaseScreenplayPoster(
                screenplayId = screenplayImages.screenplayId.toDatabaseId(),
                isPrimary = poster.isPrimary,
                path = poster.path
            )
        }
    }
}
