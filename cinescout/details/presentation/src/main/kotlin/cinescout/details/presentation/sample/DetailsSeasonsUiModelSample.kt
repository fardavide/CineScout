package cinescout.details.presentation.sample

import arrow.core.nonEmptyListOf
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.resources.R.plurals
import cinescout.resources.TextRes

object DetailsSeasonsUiModelSample {

    val BreakingBad_OneSeasonAndTwoEpisodesWatched = DetailsSeasonsUiModel(
        progress = 0.5f,
        seasonUiModels = nonEmptyListOf(
            DetailsSeasonUiModelSample.BreakingBad_s0_Unwatched,
            DetailsSeasonUiModelSample.BreakingBad_s1_Completed,
            DetailsSeasonUiModelSample.BreakingBad_s2_OneEpisodeWatched
        ),
        totalSeasons = TextRes.plural(plurals.details_total_seasons, 2, 2),
        watchedSeasons = TextRes.plural(plurals.details_watched, 1, 1)
    )
}
