package cinescout.tvshows.domain.testdata

import cinescout.common.model.TmdbVideo
import cinescout.common.model.TmdbVideoId
import cinescout.tvshows.domain.model.TvShowVideos

object TvShowVideosTestData {

    val BreakingBad = TvShowVideos(
        tvShowId = TmdbTvShowIdTestData.BreakingBad,
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

    val Grimm = TvShowVideos(
        tvShowId = TmdbTvShowIdTestData.Grimm,
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