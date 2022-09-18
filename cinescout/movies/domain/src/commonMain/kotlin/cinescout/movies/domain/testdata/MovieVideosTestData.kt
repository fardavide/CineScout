package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.TmdbVideo
import cinescout.movies.domain.model.TmdbVideoId

object MovieVideosTestData {

    val Inception = MovieVideos(
        movieId = MovieTestData.Inception.tmdbId,
        videos = listOf(
            TmdbVideo(
                id = TmdbVideoId("533ec654c3a36854480003eb"),
                key = "YoHD9XEInc0",
                resolution = TmdbVideo.Resolution.FHD,
                site = TmdbVideo.Site.YouTube,
                title = "Inception - Official Trailer [HD]",
                type = TmdbVideo.Type.Trailer
            )
        )
    )

    val TheWolfOfWallStreet = MovieVideos(
        movieId = MovieTestData.TheWolfOfWallStreet.tmdbId,
        videos = listOf(
            TmdbVideo(
                id = TmdbVideoId("533ec654c3a36854480003eb"),
                key = "iszwuX1AK6A",
                resolution = TmdbVideo.Resolution.FHD,
                site = TmdbVideo.Site.YouTube,
                title = "The Wolf of Wall Street - Official Trailer [HD]",
                type = TmdbVideo.Type.Trailer
            )
        )
    )
}
