package cinescout.details.presentation.sample

import arrow.core.nonEmptyListOf
import cinescout.details.presentation.model.DetailsSeasonUiModel
import cinescout.resources.R.plurals
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.seasons.domain.sample.SeasonSample

object DetailsSeasonUiModelSample {

    val BreakingBad_s0_Unwatched = DetailsSeasonUiModel(
        completed = false,
        episodeUiModels = nonEmptyListOf(
            DetailsEpisodeUiModelSample.BreakingBad_s0e1_Unwatched,
            DetailsEpisodeUiModelSample.BreakingBad_s0e2_Unwatched,
            DetailsEpisodeUiModelSample.BreakingBad_s0e3_Unwatched
        ),
        progress = 0f,
        seasonIds = SeasonSample.BreakingBad_s0.ids,
        title = SeasonSample.BreakingBad_s0.title,
        totalEpisodes = TextRes.plural(plurals.details_total_episodes, 3, 3),
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watchedEpisodes = TextRes.plural(plurals.details_watched, 0, 0)
    )

    val BreakingBad_s1_Completed = DetailsSeasonUiModel(
        completed = true,
        episodeUiModels = nonEmptyListOf(
            DetailsEpisodeUiModelSample.BreakingBad_s1e1_Watched,
            DetailsEpisodeUiModelSample.BreakingBad_s1e2_Watched,
            DetailsEpisodeUiModelSample.BreakingBad_s1e3_Watched
        ),
        progress = 1f,
        seasonIds = SeasonSample.BreakingBad_s1.ids,
        title = SeasonSample.BreakingBad_s1.title,
        totalEpisodes = TextRes.plural(plurals.details_total_episodes, 3, 3),
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watchedEpisodes = TextRes.plural(plurals.details_watched, 1, 1)
    )

    val BreakingBad_s2_OneEpisodeWatched = DetailsSeasonUiModel(
        completed = false,
        episodeUiModels = nonEmptyListOf(
            DetailsEpisodeUiModelSample.BreakingBad_s2e1_Watched,
            DetailsEpisodeUiModelSample.BreakingBad_s2e2_Unwatched,
            DetailsEpisodeUiModelSample.BreakingBad_s2e3_Unwatched
        ),
        progress = 0.33f,
        seasonIds = SeasonSample.BreakingBad_s2.ids,
        title = SeasonSample.BreakingBad_s2.title,
        totalEpisodes = TextRes.plural(plurals.details_total_episodes, 3, 3),
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watchedEpisodes = TextRes.plural(plurals.details_watched, 1, 1)
    )
}
