package cinescout.details.presentation.model

import arrow.core.Nel
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.id.EpisodeIds
import cinescout.screenplay.domain.model.id.SeasonIds
import cinescout.screenplay.domain.model.id.TvShowIds

data class DetailsSeasonsUiModel(
    val progress: Float,
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalSeasons: TextRes,
    val tvShowIds: TvShowIds,
    val watchedSeasons: TextRes
)

data class DetailsSeasonUiModel(
    val allReleased: Boolean,
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
    val firstAirDate: String,
    val released: Boolean,
    val seasonAndEpisodeNumber: SeasonAndEpisodeNumber,
    val title: String,
    val tvShowIds: TvShowIds,
    val watched: Boolean
)
