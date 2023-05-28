package cinescout.media.domain.sample

import cinescout.media.domain.model.MovieVideos
import cinescout.media.domain.model.TmdbVideo
import cinescout.media.domain.model.TmdbVideoId
import cinescout.media.domain.model.TvShowVideos
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object ScreenplayVideosSample {

    val Avatar3 = MovieVideos(
        screenplayId = TmdbScreenplayIdSample.Avatar3,
        videos = emptyList()
    )

    val BreakingBad = TvShowVideos(
        screenplayId = TmdbScreenplayIdSample.BreakingBad,
        videos = listOf(
            TmdbVideo(
                id = TmdbVideoId("5759db2fc3a3683e7c003df7"),
                key = "XZ8daibM3AE",
                resolution = TmdbVideo.Resolution.SD,
                site = TmdbVideo.Site.YouTube,
                title = "Series Trailer",
                type = TmdbVideo.Type.Trailer
            )
        )
    )

    val Dexter = TvShowVideos(
        screenplayId = TmdbScreenplayIdSample.Dexter,
        videos = listOf(
            TmdbVideo(
                id = TmdbVideoId("58fa28df925141587d00cd51"),
                key = "YQeUmSD1c3g",
                resolution = TmdbVideo.Resolution.SD,
                site = TmdbVideo.Site.YouTube,
                title = "Dexter | Official Trailer | SHOWTIME Series",
                type = TmdbVideo.Type.Trailer
            )
        )
    )

    val Grimm = TvShowVideos(
        screenplayId = TmdbScreenplayIdSample.Grimm,
        videos = listOf(
            TmdbVideo(
                id = TmdbVideoId("552cefcd92514103ce002b9e"),
                key = "ElSANtJSrWQ",
                resolution = TmdbVideo.Resolution.SD,
                site = TmdbVideo.Site.YouTube,
                title = "Grimm Season 2 Opening",
                type = TmdbVideo.Type.OpeningCredits
            ),
            TmdbVideo(
                id = TmdbVideoId("556032129251417e5c004cc7"),
                key = "2rVy3RBJmNo",
                resolution = TmdbVideo.Resolution.SD,
                site = TmdbVideo.Site.YouTube,
                title = "Grimm - Full Trailer",
                type = TmdbVideo.Type.Trailer
            )
        )
    )

    val Inception = MovieVideos(
        screenplayId = TmdbScreenplayIdSample.Inception,
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
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet,
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

    val War = MovieVideos(
        screenplayId = TmdbScreenplayIdSample.War,
        videos = emptyList()
    )
}
