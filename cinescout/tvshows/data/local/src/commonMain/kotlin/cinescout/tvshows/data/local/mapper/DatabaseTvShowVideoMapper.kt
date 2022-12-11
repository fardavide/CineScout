package cinescout.tvshows.data.local.mapper

import cinescout.common.model.TmdbVideo
import cinescout.database.model.DatabaseTmdbTvShowVideo
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowVideos
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseTvShowVideoMapper {

    fun toVideos(
        tvShowId: TmdbTvShowId,
        videos: List<DatabaseTmdbTvShowVideo>
    ) = TvShowVideos(
        tvShowId = tvShowId,
        videos = videos.map(::toVideo)
    )

    fun toVideo(
        video: DatabaseTmdbTvShowVideo
    ) = TmdbVideo(
        id = video.id.toId(),
        key = video.key,
        resolution = video.resolution.toVideoResolution(),
        site = video.site.toVideoSite(),
        title = video.name,
        type = video.type.toVideoType()
    )
}
