package cinescout.tvshows.domain.testdata

import cinescout.screenplay.domain.model.TmdbVideo
import cinescout.screenplay.domain.model.TmdbVideoId
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample

object TvShowVideosTestData {

    val BreakingBad = TvShowVideos(
        tvShowId = TmdbTvShowIdSample.BreakingBad,
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
        tvShowId = TmdbTvShowIdSample.Dexter,
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
        tvShowId = TmdbTvShowIdSample.Grimm,
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
}
