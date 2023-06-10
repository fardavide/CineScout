package cinescout.progress.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.model.percent
import cinescout.progress.domain.model.SeasonProgress
import cinescout.seasons.domain.sample.SeasonSample

object SeasonProgressSample {

    val BreakingBad_s0_Unwatched = SeasonProgress.Unwatched(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s0e1_Unwatched,
            EpisodeProgressSample.BreakingBad_s0e2_Unwatched,
            EpisodeProgressSample.BreakingBad_s0e3_Unwatched
        ),
        season = SeasonSample.BreakingBad_s0
    )

    val BreakingBad_s0_Completed = SeasonProgress.Completed(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s0e1_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s0e2_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s0e3_WatchedOnce
        ),
        season = SeasonSample.BreakingBad_s0
    )

    val BreakingBad_s1_Unwatched = SeasonProgress.Unwatched(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s1e1_Unwatched,
            EpisodeProgressSample.BreakingBad_s1e2_Unwatched,
            EpisodeProgressSample.BreakingBad_s1e3_Unwatched
        ),
        season = SeasonSample.BreakingBad_s1
    )

    val BreakingBad_s1_Completed = SeasonProgress.Completed(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s1e1_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s1e2_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s1e3_WatchedOnce
        ),
        season = SeasonSample.BreakingBad_s1
    )

    val BreakingBad_s2_Unwatched = SeasonProgress.Unwatched(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s2e1_Unwatched,
            EpisodeProgressSample.BreakingBad_s2e2_Unwatched,
            EpisodeProgressSample.BreakingBad_s2e3_Unwatched
        ),
        season = SeasonSample.BreakingBad_s2
    )

    val BreakingBad_s2_InProgress_OneEpisodeWatched = SeasonProgress.InProgress(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s2e1_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s2e2_Unwatched,
            EpisodeProgressSample.BreakingBad_s2e3_Unwatched
        ),
        progress = 23.08.percent,
        season = SeasonSample.BreakingBad_s2
    )

    val BreakingBad_s2_Completed = SeasonProgress.Completed(
        episodesProgress = nonEmptyListOf(
            EpisodeProgressSample.BreakingBad_s2e1_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s2e2_WatchedOnce,
            EpisodeProgressSample.BreakingBad_s2e3_WatchedOnce
        ),
        season = SeasonSample.BreakingBad_s2
    )
}
