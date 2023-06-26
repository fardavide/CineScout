package cinescout.screenplay.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.screenplay.domain.model.MovieGenres
import cinescout.screenplay.domain.model.TvShowGenres

object ScreenplayGenresSample {

    val Avatar3 = MovieGenres(
        screenplayIds = ScreenplayIdsSample.Avatar3,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.ScienceFiction, GenreSample.Adventure)
    )

    val BreakingBad = TvShowGenres(
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        genres = nonEmptyListOf(GenreSample.Drama)
    )

    val Dexter = TvShowGenres(
        screenplayIds = ScreenplayIdsSample.Dexter,
        genres = nonEmptyListOf(GenreSample.Crime, GenreSample.Drama, GenreSample.Mystery)
    )

    val Grimm = TvShowGenres(
        screenplayIds = ScreenplayIdsSample.Grimm,
        genres = nonEmptyListOf(GenreSample.Drama, GenreSample.Fantasy, GenreSample.Mystery, GenreSample.ScienceFiction)
    )

    val Inception = MovieGenres(
        screenplayIds = ScreenplayIdsSample.Inception,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )

    val TheWalkingDeadDeadCity = TvShowGenres(
        screenplayIds = ScreenplayIdsSample.TheWalkingDeadDeadCity,
        genres = nonEmptyListOf(
            GenreSample.Action,
            GenreSample.Adventure,
            GenreSample.Fantasy,
            GenreSample.Drama,
            GenreSample.ScienceFiction
        )
    )

    val TheWolfOfWallStreet = MovieGenres(
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Crime, GenreSample.Drama)
    )

    val War = MovieGenres(
        screenplayIds = ScreenplayIdsSample.War,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )
}
