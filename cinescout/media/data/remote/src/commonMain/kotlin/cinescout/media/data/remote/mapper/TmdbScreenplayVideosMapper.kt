package cinescout.media.data.remote.mapper

import cinescout.media.data.remote.model.GetScreenplayVideosResponse
import cinescout.media.data.remote.model.GetScreenplayVideosResponseWithId
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.media.domain.model.TmdbVideo
import cinescout.media.domain.model.TmdbVideoId
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayVideosMapper {

    fun toScreenplayVideos(videos: GetScreenplayVideosResponseWithId) = ScreenplayVideos(
        screenplayId = videos.screenplayId,
        videos = videos.response.videos.map(::toVideo)
    )

    private fun toVideo(video: GetScreenplayVideosResponse.Video): TmdbVideo {
        val resolution = when {
            video.size < 1080 -> TmdbVideo.Resolution.SD
            video.size == 1080 -> TmdbVideo.Resolution.FHD
            video.size == 2160 -> TmdbVideo.Resolution.UHD
            else -> throw IllegalArgumentException("Unknown video resolution: ${video.size}")
        }
        val site = when (video.site) {
            "YouTube" -> TmdbVideo.Site.YouTube
            else -> throw IllegalArgumentException("Unknown video site: ${video.site}")
        }
        val type = when (video.type) {
            "Behind the Scenes" -> TmdbVideo.Type.BehindTheScenes
            "Bloopers" -> TmdbVideo.Type.Bloopers
            "Clip" -> TmdbVideo.Type.Clip
            "Featurette" -> TmdbVideo.Type.Featurette
            "Opening Credits", "OpeningCredits" -> TmdbVideo.Type.OpeningCredits
            "Teaser" -> TmdbVideo.Type.Teaser
            "Trailer" -> TmdbVideo.Type.Trailer
            else -> throw IllegalArgumentException("Unknown video type: ${video.type}")
        }
        return TmdbVideo(
            id = TmdbVideoId(video.id),
            key = video.key,
            site = site,
            resolution = resolution,
            title = video.name,
            type = type
        )
    }
}
