package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.domain.testdata.MovieWithDetailsTestData
import com.soywiz.klock.DateFormat

object TmdbMovieDetailsJson {

    val Inception = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
        "${GetMovieDetails.Response.Genres}": [
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[0].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[0].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[1].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[1].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[2].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[2].name}"
            }
        ],
        "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
        "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
    }
    """

    val InceptionWithEmptyGenres = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
        "${GetMovieDetails.Response.Genres}": [],
        "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
        "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
    }
    """

    val InceptionWithoutGenres = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
        "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
        "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
    }
    """

    val InceptionWithoutReleaseDate = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
        "${GetMovieDetails.Response.Genres}": [
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[0].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[0].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[1].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[1].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": "${MovieWithDetailsTestData.Inception.genres[2].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": "${MovieWithDetailsTestData.Inception.genres[2].name}"
            }
        ],
        "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
    }
    """

    val TheWolfOfWallStreet = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.backdropPath}",
        "${GetMovieDetails.Response.Genres}": [
            {
                "${GetMovieDetails.Response.Genre.Id}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[0].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[0].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[1].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[1].name}"
            },
            {
                "${GetMovieDetails.Response.Genre.Id}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[2].id.value}",
                "${GetMovieDetails.Response.Genre.Name}": 
                    "${MovieWithDetailsTestData.TheWolfOfWallStreet.genres[2].name}"
            }
        ],
        "${TmdbMovie.Id}": "${TmdbMovieTestData.TheWolfOfWallStreet.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.posterPath}",
        "${TmdbMovie.ReleaseDate}": 
            "${TmdbMovieTestData.TheWolfOfWallStreet.releaseDate?.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.TheWolfOfWallStreet.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteCount}"
    }
    """
}
