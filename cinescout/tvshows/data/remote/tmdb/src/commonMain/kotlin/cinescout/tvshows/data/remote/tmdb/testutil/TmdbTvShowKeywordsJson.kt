package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowKeywords.Response
import cinescout.tvshows.domain.testdata.TvShowKeywordsTestData

object TmdbTvShowKeywordsJson {

    val Grimm = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.Grimm.tvShowId.value}",
            "${Response.Results}": [
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.Grimm.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.Grimm.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.Grimm.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.Grimm.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.Grimm.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.Grimm.keywords[2].name}"
                }
            ]
        }
    """

    val BreakingBad = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.BreakingBad.tvShowId.value}",
            "${Response.Results}": [
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.BreakingBad.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.BreakingBad.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.BreakingBad.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.BreakingBad.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${TvShowKeywordsTestData.BreakingBad.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${TvShowKeywordsTestData.BreakingBad.keywords[2].name}"
                }
            ]
        }
    """
}
