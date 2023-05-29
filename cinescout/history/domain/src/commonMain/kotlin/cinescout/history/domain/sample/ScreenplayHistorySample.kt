package cinescout.history.domain.sample

import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.model.TvShowHistoryState
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object ScreenplayHistorySample {

    val BreakingBad = TvShowHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.Completed
    )

    val Dexter = TvShowHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.Dexter,
        state = TvShowHistoryState.Completed
    )

    val Grimm = TvShowHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.Grimm,
        state = TvShowHistoryState.Completed
    )

    val Inception = MovieHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.Inception
    )

    val TheWolfOfWallStreet = MovieHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet
    )

    val War = MovieHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.War
    )
}
