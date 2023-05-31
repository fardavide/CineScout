package cinescout.history.domain.sample

import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.model.TvShowHistoryState
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object HistorySample {

    val BreakingBad = TvShowHistory(
        items = listOf(
            ScreenplayHistoryItemSample.BreakingBad_s1e1,
            ScreenplayHistoryItemSample.BreakingBad_s1e2
        ),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.InProgress
    )

    val Dexter = TvShowHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.Dexter,
        state = TvShowHistoryState.InProgress
    )

    val Grimm = TvShowHistory(
        items = emptyList(),
        screenplayIds = ScreenplayIdsSample.Grimm,
        state = TvShowHistoryState.InProgress
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
