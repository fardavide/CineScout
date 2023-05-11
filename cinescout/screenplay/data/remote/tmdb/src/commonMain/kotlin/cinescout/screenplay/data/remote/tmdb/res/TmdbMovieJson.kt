package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.data.remote.tmdb.sample.TmdbMovieSample
import cinescout.screenplay.data.remote.tmdb.sample.TmdbTvShowSample
import korlibs.time.DateFormat

object TmdbMovieJson {

    val BreakingBad = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.BreakingBad.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.BreakingBad.id.value}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.BreakingBad.overview}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.BreakingBad.title}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.BreakingBad.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.BreakingBad.voteCount}"
        }
    """.trimIndent()

    val Grimm = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.Grimm.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.Grimm.id.value}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.Grimm.overview}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.Grimm.title}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.Grimm.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.Grimm.voteCount}"
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
}
