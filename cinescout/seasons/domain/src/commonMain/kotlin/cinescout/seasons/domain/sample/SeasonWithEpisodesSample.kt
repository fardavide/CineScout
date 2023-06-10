package cinescout.seasons.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.seasons.domain.model.SeasonWithEpisodes

object SeasonWithEpisodesSample {

    val BreakingBad_0 = SeasonWithEpisodes(
        episodes = nonEmptyListOf(
            EpisodeSample.BreakingBad_s0e1,
            EpisodeSample.BreakingBad_s0e2,
            EpisodeSample.BreakingBad_s0e3
        ),
        season = SeasonSample.BreakingBad_s0
    )

    val BreakingBad_1 = SeasonWithEpisodes(
        episodes = nonEmptyListOf(
            EpisodeSample.BreakingBad_s1e1,
            EpisodeSample.BreakingBad_s1e2,
            EpisodeSample.BreakingBad_s1e3
        ),
        season = SeasonSample.BreakingBad_s1
    )

    val BreakingBad_2 = SeasonWithEpisodes(
        episodes = nonEmptyListOf(
            EpisodeSample.BreakingBad_s2e1,
            EpisodeSample.BreakingBad_s2e2,
            EpisodeSample.BreakingBad_s2e3
        ),
        season = SeasonSample.BreakingBad_s2
    )
}
