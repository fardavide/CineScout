package cinescout.details.presentation.model

import arrow.core.Nel
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.screenplay.domain.model.ids.TvShowIds

data class DetailsSeasonsUiModel(
    val progress: Float,
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalSeasons: TextRes,
    val tvShowIds: TvShowIds,
    val watchedSeasons: TextRes
)

data class DetailsSeasonUiModel(
    val completed: Boolean,
    val episodeUiModels: List<DetailsEpisodeUiModel>,
    val progress: Float,
    val seasonIds: SeasonIds,
    val title: String,
    val totalEpisodes: TextRes,
    val tvShowIds: TvShowIds,
    val watchedEpisodes: TextRes
)

data class DetailsEpisodeUiModel(
    val episodeIds: EpisodeIds,
    val episodeNumber: TextRes,
    val seasonAndEpisodeNumber: SeasonAndEpisodeNumber,
    val title: String,
    val tvShowIds: TvShowIds,
    val watched: Boolean
)
