package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowVideos.Response
import cinescout.tvshows.domain.testdata.TvShowKeywordsTestData
import cinescout.tvshows.domain.testdata.TvShowVideosTestData

object TmdbTvShowVideosJson {

    val BreakingBad = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.BreakingBad.tvShowId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${TvShowVideosTestData.BreakingBad.videos[0].id.value}",
                    "${Response.Video.Key}": "${TvShowVideosTestData.BreakingBad.videos[0].key}",
                    "${Response.Video.Name}": "${TvShowVideosTestData.BreakingBad.videos[0].title}",
                    "${Response.Video.Site}": "${TvShowVideosTestData.BreakingBad.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${TvShowVideosTestData.BreakingBad.videos[0].type.name}"
                }
            ]
        }
    """

    val Dexter = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.Dexter.tvShowId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${TvShowVideosTestData.Dexter.videos[0].id.value}",
                    "${Response.Video.Key}": "${TvShowVideosTestData.Dexter.videos[0].key}",
                    "${Response.Video.Name}": "${TvShowVideosTestData.Dexter.videos[0].title}",
                    "${Response.Video.Site}": "${TvShowVideosTestData.Dexter.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${TvShowVideosTestData.Dexter.videos[0].type.name}"
                }
            ]
        }
    """

    val Grimm = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.Grimm.tvShowId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${TvShowVideosTestData.Grimm.videos[0].id.value}",
                    "${Response.Video.Key}": "${TvShowVideosTestData.Grimm.videos[0].key}",
                    "${Response.Video.Name}": "${TvShowVideosTestData.Grimm.videos[0].title}",
                    "${Response.Video.Site}": "${TvShowVideosTestData.Grimm.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${TvShowVideosTestData.Grimm.videos[0].type.name}"
                }
            ]
        }
    """
}
