package cinescout.details.presentation.model

import arrow.core.Nel
import cinescout.resources.TextRes

data class DetailsSeasonsUiModel(
    val progress: Float,
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalSeasons: TextRes,
    val watchedSeasons: TextRes
)

data class DetailsSeasonUiModel(
    val completed: Boolean,
    val episodeUiModels: List<DetailsEpisodeUiModel>,
    val progress: Float,
    val title: String,
    val totalEpisodes: TextRes,
    val watchedEpisodes: TextRes
)

data class DetailsEpisodeUiModel(
    val episodeNumber: TextRes,
    val title: String,
    val watched: Boolean
)
