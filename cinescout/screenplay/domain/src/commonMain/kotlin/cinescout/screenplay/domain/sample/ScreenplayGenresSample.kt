package cinescout.screenplay.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.screenplay.domain.model.MovieGenres
import cinescout.screenplay.domain.model.TvShowGenres

object ScreenplayGenresSample {

    val Avatar3 = MovieGenres(
        screenplayId = TmdbScreenplayIdSample.Avatar3,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.ScienceFiction, GenreSample.Adventure)
    )

    val BreakingBad = TvShowGenres(
        screenplayId = TmdbScreenplayIdSample.BreakingBad,
        genres = nonEmptyListOf(GenreSample.Drama)
    )

    val Dexter = TvShowGenres(
        screenplayId = TmdbScreenplayIdSample.Dexter,
        genres = nonEmptyListOf(GenreSample.Crime, GenreSample.Drama, GenreSample.Mystery)
    )

    val Grimm = TvShowGenres(
        screenplayId = TmdbScreenplayIdSample.Grimm,
        genres = nonEmptyListOf(GenreSample.Drama, GenreSample.Mystery, GenreSample.SciFiFantasy)
    )

    val Inception = MovieGenres(
        screenplayId = TmdbScreenplayIdSample.Inception,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )

    val TheWalkingDeadDeadCity = TvShowGenres(
        screenplayId = TmdbScreenplayIdSample.TheWalkingDeadDeadCity,
        genres = nonEmptyListOf(GenreSample.ActionAdventure, GenreSample.Drama, GenreSample.SciFiFantasy)
    )

    val TheWolfOfWallStreet = MovieGenres(
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Crime, GenreSample.Drama)
    )

    val War = MovieGenres(
        screenplayId = TmdbScreenplayIdSample.War,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )
}
