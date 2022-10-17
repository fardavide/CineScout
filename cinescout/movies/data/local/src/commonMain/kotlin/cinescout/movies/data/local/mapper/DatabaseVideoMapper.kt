package cinescout.movies.data.local.mapper

import cinescout.common.model.TmdbVideo
import cinescout.database.model.DatabaseTmdbVideo
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.TmdbMovieId

class DatabaseVideoMapper {

    fun toVideos(
        movieId: TmdbMovieId,
        videos: List<DatabaseTmdbVideo>
    ) = MovieVideos(
        movieId = movieId,
        videos = videos.map(::toVideo)
    )

    fun toVideo(
        video: DatabaseTmdbVideo
    ) = TmdbVideo(
        id = video.id.toId(),
        key = video.key,
        resolution = video.resolution.toVideoResolution(),
        site = video.site.toVideoSite(),
        title = video.name,
        type = video.type.toVideoType()
    )
}
