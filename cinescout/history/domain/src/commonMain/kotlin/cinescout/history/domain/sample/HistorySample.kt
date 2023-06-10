package cinescout.history.domain.sample

import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.model.TvShowHistoryState
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object HistorySample {

    val BreakingBad_InProgress_OneSeasonCompleted = TvShowHistory(
        items = listOf(
            ScreenplayHistoryItemSample.BreakingBad_s1e1,
            ScreenplayHistoryItemSample.BreakingBad_s1e2,
            ScreenplayHistoryItemSample.BreakingBad_s1e3
        ),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.InProgress
    )

    val BreakingBad_InProgress_OneSeasonCompletedAndOneEpisodeWatched = TvShowHistory(
        items = listOf(
            ScreenplayHistoryItemSample.BreakingBad_s1e1,
            ScreenplayHistoryItemSample.BreakingBad_s1e2,
            ScreenplayHistoryItemSample.BreakingBad_s1e3,
            ScreenplayHistoryItemSample.BreakingBad_s2e1
        ),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.InProgress
    )

    val BreakingBad_AllWatchedOnce = TvShowHistory(
        items = listOf(
            ScreenplayHistoryItemSample.BreakingBad_s0e1,
            ScreenplayHistoryItemSample.BreakingBad_s0e2,
            ScreenplayHistoryItemSample.BreakingBad_s0e3,
            ScreenplayHistoryItemSample.BreakingBad_s1e1,
            ScreenplayHistoryItemSample.BreakingBad_s1e2,
            ScreenplayHistoryItemSample.BreakingBad_s1e3,
            ScreenplayHistoryItemSample.BreakingBad_s2e1,
            ScreenplayHistoryItemSample.BreakingBad_s2e2,
            ScreenplayHistoryItemSample.BreakingBad_s2e3
        ),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.Completed
    )

    val BreakingBad_WatchedOnceWithoutSpecials = TvShowHistory(
        items = listOf(
            ScreenplayHistoryItemSample.BreakingBad_s1e1,
            ScreenplayHistoryItemSample.BreakingBad_s1e2,
            ScreenplayHistoryItemSample.BreakingBad_s1e3,
            ScreenplayHistoryItemSample.BreakingBad_s2e1,
            ScreenplayHistoryItemSample.BreakingBad_s2e2,
            ScreenplayHistoryItemSample.BreakingBad_s2e3
        ),
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        state = TvShowHistoryState.Completed
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
