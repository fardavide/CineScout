package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowImages.Response
import cinescout.tvshows.domain.testdata.TvShowImagesTestData
import cinescout.tvshows.domain.testdata.TvShowKeywordsTestData

object TmdbTvShowImagesJson {

    val BreakingBad = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.BreakingBad.tvShowId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.BreakingBad.backdrops[2].path}"
                }
            ]
        }
    """

    val Dexter = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.Dexter.tvShowId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Dexter.backdrops[2].path}"
                }
            ]
        }
    """

    val Grimm = """
        {
            "${Response.TvShowId}": "${TvShowKeywordsTestData.Grimm.tvShowId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${TvShowImagesTestData.Grimm.backdrops[2].path}"
                }
            ]
        }
    """
}
