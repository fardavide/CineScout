package cinescout.details.presentation.model

import arrow.core.Nel
import arrow.optics.copy
import arrow.optics.optics
import cinescout.history.domain.usecase.AddToHistory
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.screenplay.domain.model.ids.TvShowIds

@optics data class DetailsSeasonsUiModel(
    val progress: Float,
    val seasonUiModels: Nel<DetailsSeasonUiModel>,
    val totalSeasons: TextRes,
    val tvShowIds: TvShowIds,
    val watchedSeasons: TextRes
) {
    companion object
}

fun DetailsSeasonsUiModel.updateWithHistory(addToHistoryParam: AddToHistory.Params): DetailsSeasonsUiModel =
    copy {
        if (addToHistoryParam is AddToHistory.Params.TvShow && addToHistoryParam.tvShowIds == tvShowIds) {
            DetailsSeasonsUiModel.seasonUiModels set seasonUiModels.map { it.updateAllCompleted() }
        } else {
            DetailsSeasonsUiModel.seasonUiModels set seasonUiModels.map { it.updateWithHistory(addToHistoryParam) }
        }
    }

@optics data class DetailsSeasonUiModel(
    val completed: Boolean,
    val episodeUiModels: List<DetailsEpisodeUiModel>,
    val progress: Float,
    val seasonIds: SeasonIds,
    val title: String,
    val totalEpisodes: TextRes,
    val tvShowIds: TvShowIds,
    val watchedEpisodes: TextRes
) {
    companion object
}

fun DetailsSeasonUiModel.updateWithHistory(addToHistoryParam: AddToHistory.Params): DetailsSeasonUiModel =
    copy {
        if (addToHistoryParam is AddToHistory.Params.Season && addToHistoryParam.seasonIds == seasonIds) {
            DetailsSeasonUiModel.completed set true
            DetailsSeasonUiModel.episodeUiModels set episodeUiModels.map { it.updateWatched() }
        } else if (addToHistoryParam is AddToHistory.Params.Episode) {
            DetailsSeasonUiModel.episodeUiModels set episodeUiModels.map { it.updateWithHistory(addToHistoryParam) }
        }
    }

fun DetailsSeasonUiModel.updateAllCompleted(): DetailsSeasonUiModel = copy {
    DetailsSeasonUiModel.completed set true
    DetailsSeasonUiModel.episodeUiModels set episodeUiModels.map { it.updateWatched() }
}

@optics data class DetailsEpisodeUiModel(
    val episodeIds: EpisodeIds,
    val episodeNumber: TextRes,
    val seasonAndEpisodeNumber: SeasonAndEpisodeNumber,
    val title: String,
    val tvShowIds: TvShowIds,
    val watched: Boolean
) {
    companion object
}

fun DetailsEpisodeUiModel.updateWithHistory(addToHistoryParam: AddToHistory.Params): DetailsEpisodeUiModel =
    copy {
        if (addToHistoryParam is AddToHistory.Params.Episode && addToHistoryParam.episodeIds == episodeIds) {
            DetailsEpisodeUiModel.watched set true
        }
    }

fun DetailsEpisodeUiModel.updateWatched(): DetailsEpisodeUiModel = copy {
    DetailsEpisodeUiModel.watched set true
}
