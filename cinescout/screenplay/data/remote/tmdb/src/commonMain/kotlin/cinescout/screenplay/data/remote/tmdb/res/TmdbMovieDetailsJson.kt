package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.sample.TmdbMovieSample
import korlibs.time.DateFormat

object TmdbMovieDetailsJson {

    val Avatar3 = """
        {
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.Avatar3.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Avatar3.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Avatar3.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Avatar3.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Avatar3.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Avatar3.voteCount}"
        }
    """.trimIndent()

    val Inception = """
        {
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.Inception.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.TheWolfOfWallStreet.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.TheWolfOfWallStreet.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.TheWolfOfWallStreet.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.TheWolfOfWallStreet.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.TheWolfOfWallStreet.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.TheWolfOfWallStreet.voteCount}"
        }
    """.trimIndent()

    val War = """
        {
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.War.id.value}",
            "${TmdbMovie.Overview}": "${TmdbMovieSample.War.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.War.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbMovie.Title}": "${TmdbMovieSample.War.title}",
            "${TmdbMovie.VoteAverage}": "${TmdbMovieSample.War.voteAverage}",
            "${TmdbMovie.VoteCount}": "${TmdbMovieSample.War.voteCount}"
        }
    """.trimIndent()
}
