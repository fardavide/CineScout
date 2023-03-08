package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.Screenplay

object ScreenplaySample {

    val BreakingBad = Screenplay.TvShow(
        title = "Breaking Bad",
        tmdbId = TmdbScreenplayIdSample.BreakingBad
    )
    val Dexter = Screenplay.TvShow(
        title = "Dexter",
        tmdbId = TmdbScreenplayIdSample.Dexter
    )
    val Grimm = Screenplay.TvShow(
        title = "Grimm",
        tmdbId = TmdbScreenplayIdSample.Grimm
    )
    val Inception = Screenplay.Movie(
        title = "Inception",
        tmdbId = TmdbScreenplayIdSample.Inception
    )
    val TheWolfOfWallStreet = Screenplay.Movie(
        title = "The Wolf of Wall Street",
        tmdbId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )
    val War = Screenplay.Movie(
        title = "War",
        tmdbId = TmdbScreenplayIdSample.War
    )
}
