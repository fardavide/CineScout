package cinescout.progress.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.model.percent
import cinescout.progress.domain.model.TvShowProgress
import cinescout.screenplay.domain.sample.ScreenplaySample

object TvShowProgressSample {

    val BreakingBad_Unwatched = TvShowProgress.Unwatched(
        screenplay = ScreenplaySample.BreakingBad,
        seasonsProgress = nonEmptyListOf(
            SeasonProgressSample.BreakingBad_s0_Unwatched,
            SeasonProgressSample.BreakingBad_s1_Unwatched,
            SeasonProgressSample.BreakingBad_s2_Unwatched
        )
    )

    val BreakingBad_Completed = TvShowProgress.Completed(
        screenplay = ScreenplaySample.BreakingBad,
        seasonsProgress = nonEmptyListOf(
            SeasonProgressSample.BreakingBad_s0_Completed,
            SeasonProgressSample.BreakingBad_s1_Completed,
            SeasonProgressSample.BreakingBad_s2_Completed
        )
    )

    val BreakingBad_CompletedWithoutSpecials = TvShowProgress.Completed(
        screenplay = ScreenplaySample.BreakingBad,
        seasonsProgress = nonEmptyListOf(
            SeasonProgressSample.BreakingBad_s0_Unwatched,
            SeasonProgressSample.BreakingBad_s1_Completed,
            SeasonProgressSample.BreakingBad_s2_Completed
        )
    )

    val BreakingBad_InProgress_OneSeasonWatched = TvShowProgress.InProgress(
        progress = 50.percent,
        screenplay = ScreenplaySample.BreakingBad,
        seasonsProgress = nonEmptyListOf(
            SeasonProgressSample.BreakingBad_s0_Unwatched,
            SeasonProgressSample.BreakingBad_s1_Completed,
            SeasonProgressSample.BreakingBad_s2_Unwatched
        )
    )

    val BreakingBad_InProgress_OneSeasonAndOneEpisodeWatched = TvShowProgress.InProgress(
        progress = 66.67.percent,
        screenplay = ScreenplaySample.BreakingBad,
        seasonsProgress = nonEmptyListOf(
            SeasonProgressSample.BreakingBad_s0_Unwatched,
            SeasonProgressSample.BreakingBad_s1_Completed,
            SeasonProgressSample.BreakingBad_s2_InProgress_OneEpisodeWatched
        )
    )
}
