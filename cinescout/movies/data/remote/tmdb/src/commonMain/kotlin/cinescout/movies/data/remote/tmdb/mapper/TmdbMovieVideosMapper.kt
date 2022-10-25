package cinescout.movies.data.remote.tmdb.mapper

import cinescout.common.model.TmdbVideo
import cinescout.common.model.TmdbVideoId
import cinescout.movies.data.remote.tmdb.model.GetMovieVideos
import cinescout.movies.domain.model.MovieVideos

class TmdbMovieVideosMapper {

    fun toMovieVideos(videos: GetMovieVideos.Response) = MovieVideos(
        movieId = videos.movieId,
        videos = videos.videos.map(::toVideo)
    )

    private fun toVideo(video: GetMovieVideos.Response.Video): TmdbVideo {
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
