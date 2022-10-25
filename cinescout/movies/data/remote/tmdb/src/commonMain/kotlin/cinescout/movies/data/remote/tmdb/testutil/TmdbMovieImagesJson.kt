package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieImages.Response
import cinescout.movies.domain.testdata.MovieImagesTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData

object TmdbMovieImagesJson {

    val Inception = """
        {
            "${Response.MovieId}": "${MovieKeywordsTestData.Inception.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.Inception.backdrops[2].path}"
                }
            ]
        }
    """

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${MovieKeywordsTestData.TheWolfOfWallStreet.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.TheWolfOfWallStreet.backdrops[2].path}"
                }
            ]
        }
    """

    val War = """
        {
            "${Response.MovieId}": "${MovieKeywordsTestData.War.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${MovieImagesTestData.War.backdrops[2].path}"
                }
            ]
        }
    """
}
