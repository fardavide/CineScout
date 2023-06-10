package cinescout.seasons.domain.sample

import arrow.core.nonEmptyListOf
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes

object TvShowSeasonsWithEpisodesSample {

    val BreakingBad = TvShowSeasonsWithEpisodes(
        seasonsWithEpisodes = nonEmptyListOf(
            SeasonWithEpisodesSample.BreakingBad_0,
            SeasonWithEpisodesSample.BreakingBad_1,
            SeasonWithEpisodesSample.BreakingBad_2
        ),
        tvShowIds = ScreenplayIdsSample.BreakingBad
    )
}
