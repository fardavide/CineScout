package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.domain.sample.MovieWithDetailsSample
import com.soywiz.klock.DateFormat

object TmdbMovieDetailsJson {

    val Inception = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.Inception.backdropPath}",
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
            "${TmdbMovie.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithEmptyGenres = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.Inception.backdropPath}",
            "${GetMovieDetails.Response.Genres}": [],
            "${TmdbMovie.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithoutGenres = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.Inception.backdropPath}",
            "${TmdbMovie.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.Inception.posterPath}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val InceptionWithoutReleaseDate = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.Inception.backdropPath}",
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
            "${TmdbMovie.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.Inception.posterPath}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.TheWolfOfWallStreet.backdropPath}",
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
            "${TmdbMovie.Id}": "${TmdbMovieSample.TheWolfOfWallStreet.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.TheWolfOfWallStreet.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.TheWolfOfWallStreet.posterPath}",
            "${TmdbMovie.ReleaseDate}": 
                "${TmdbMovieSample.TheWolfOfWallStreet.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.TheWolfOfWallStreet.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.TheWolfOfWallStreet.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.TheWolfOfWallStreet.voteCount}"
        }
    """.trimIndent()

    val War = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.War.backdropPath}",
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
            "${TmdbMovie.Id}": "${TmdbMovieSample.War.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.War.overview}",
            "${TmdbMovie.PosterPath}": "${TmdbMovieSample.War.posterPath}",
            "${TmdbMovie.ReleaseDate}": 
                "${TmdbMovieSample.War.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.War.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.War.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.War.voteCount}"
        }
    """.trimIndent()
}
