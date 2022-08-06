package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import com.soywiz.klock.DateFormat

object TmdbMovieJson {

    val Inception = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.Inception.backdropPath}",
        "${TmdbMovie.Id}": "${TmdbMovieTestData.Inception.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.Inception.posterPath}",
        "${TmdbMovie.ReleaseDate}": "${TmdbMovieTestData.Inception.releaseDate.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.Inception.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.Inception.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.Inception.voteCount}"
    }
    """

    val TheWolfOfWallStreet = """
    {
        "${TmdbMovie.BackdropPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.backdropPath}",
        "${TmdbMovie.Id}": "${TmdbMovieTestData.TheWolfOfWallStreet.id.value}",
        "${TmdbMovie.PosterPath}": "${TmdbMovieTestData.TheWolfOfWallStreet.posterPath}",
        "${TmdbMovie.ReleaseDate}": 
            "${TmdbMovieTestData.TheWolfOfWallStreet.releaseDate.format(DateFormat.FORMAT_DATE)}",
        "${TmdbMovie.Title}": "${TmdbMovieTestData.TheWolfOfWallStreet.title}",
        "${TmdbMovie.VoteAverage}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteAverage}",
        "${TmdbMovie.VoteCount}": "${TmdbMovieTestData.TheWolfOfWallStreet.voteCount}"
    }
    """
}
