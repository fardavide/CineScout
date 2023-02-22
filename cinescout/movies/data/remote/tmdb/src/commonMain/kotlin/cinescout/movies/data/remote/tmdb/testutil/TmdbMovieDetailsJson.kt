package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.domain.sample.MovieWithDetailsSample
import com.soywiz.klock.DateFormat

object TmdbMovieDetailsJson {

    val Inception = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[0].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[0].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[1].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[1].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[2].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[2].name}"
                }
            ],
            "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithEmptyGenres = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [],
            "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithoutGenres = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
            "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithoutReleaseDate = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[0].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[0].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[1].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[1].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsSample.Inception.genres[2].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsSample.Inception.genres[2].name}"
                }
            ],
            "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[0].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[0].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[1].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[1].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[2].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.TheWolfOfWallStreet.genres[2].name}"
                }
            ],
            "${TmdbMovie.Id}": "${TmdbMovieTestData.TheWolfOfWallStreet.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.TheWolfOfWallStreet.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.posterPath}",
            "${TmdbMovie.ReleaseDate}": 
                "${TmdbMovieTestData.TheWolfOfWallStreet.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.TheWolfOfWallStreet.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteCount}"
        }
    """.trimIndent()

    val War = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.War.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.War.genres[0].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.War.genres[0].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.War.genres[1].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.War.genres[1].name}"
                },
                {
                    "${GetMovieDetails.Response.Genre.Id}": 
                        "${MovieWithDetailsSample.War.genres[2].id.value}",
                    "${GetMovieDetails.Response.Genre.Name}": 
                        "${MovieWithDetailsSample.War.genres[2].name}"
                }
            ],
            "${TmdbMovie.Id}": "${TmdbMovieTestData.War.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieTestData.War.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.War.posterPath}",
            "${TmdbMovie.ReleaseDate}": 
                "${TmdbMovieTestData.War.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieTestData.War.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.War.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.War.voteCount}"
        }
    """.trimIndent()
}
