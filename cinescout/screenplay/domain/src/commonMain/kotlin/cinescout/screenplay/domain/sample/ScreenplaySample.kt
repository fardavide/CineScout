package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TvShow

object ScreenplaySample {

    val BreakingBad = TvShow(
        title = "Breaking Bad",
        tmdbId = TmdbScreenplayIdSample.BreakingBad
    )
    val Dexter = TvShow(
        title = "Dexter",
        tmdbId = TmdbScreenplayIdSample.Dexter
    )
    val Grimm = TvShow(
        title = "Grimm",
        tmdbId = TmdbScreenplayIdSample.Grimm
    )
    val Inception = Movie(
        title = "Inception",
        tmdbId = TmdbScreenplayIdSample.Inception
    )
    val TheWolfOfWallStreet = Movie(
        title = "The Wolf of Wall Street",
        tmdbId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )
    val War = Movie(
        title = "War",
        tmdbId = TmdbScreenplayIdSample.War
    )
}
