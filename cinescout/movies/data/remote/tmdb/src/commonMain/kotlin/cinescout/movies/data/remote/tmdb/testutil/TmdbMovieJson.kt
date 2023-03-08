package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.sample.TmdbMovieSample
import com.soywiz.klock.DateFormat

object TmdbMovieJson {

    val Inception = """
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

    val TheWolfOfWallStreet = """
        {
            "${TmdbMovie.BackdropPath}": "${TmdbMovieSample.TheWolfOfWallStreet.backdropPath}",
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
}
