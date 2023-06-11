package cinescout.details.presentation.model

import arrow.core.Nel
import cinescout.resources.TextRes

data class DetailsSeasonsUiModel(
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalWatchedSeasons: TextRes
)

data class DetailsSeasonUiModel(
    val episodeUiModels: Nel<DetailsEpisodeUiModel>,
    val title: String,
    val totalWatchedEpisodes: TextRes
)

data class DetailsEpisodeUiModel(
    val title: String,
    val watched: Boolean
)
