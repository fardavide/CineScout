package cinescout.movies.data.local.mapper

import cinescout.common.model.TmdbVideo
import cinescout.database.model.DatabaseTmdbMovieVideo
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.TmdbMovieId

class DatabaseVideoMapper {

    fun toVideos(
        movieId: TmdbMovieId,
        videos: List<DatabaseTmdbMovieVideo>
    ) = MovieVideos(
        movieId = movieId,
        videos = videos.map(::toVideo)
    )

    fun toVideo(
        video: DatabaseTmdbMovieVideo
    ) = TmdbVideo(
        id = video.id.toId(),
        key = video.key,
        resolution = video.resolution.toVideoResolution(),
        site = video.site.toVideoSite(),
        title = video.name,
        type = video.type.toVideoType()
    )
}
