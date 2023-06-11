package cinescout.details.presentation.model

import arrow.core.Nel
import cinescout.resources.TextRes

data class DetailsSeasonsUiModel(
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalSeasons: TextRes,
    val watchedSeasons: TextRes
)

data class DetailsSeasonUiModel(
    val episodeUiModels: Nel<DetailsEpisodeUiModel>,
    val title: String,
    val totalEpisodes: TextRes,
    val watchedEpisodes: TextRes
)

data class DetailsEpisodeUiModel(
    val title: String,
    val watched: Boolean
)
