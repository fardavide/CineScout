package cinescout.details.presentation.mapper

import cinescout.GetCurrentDateTime
import cinescout.details.presentation.model.DetailsEpisodeUiModel
import cinescout.details.presentation.model.DetailsSeasonUiModel
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.model.Percent
import cinescout.progress.domain.model.EpisodeProgress
import cinescout.progress.domain.model.SeasonProgress
import cinescout.progress.domain.model.TvShowProgress
import cinescout.resources.R.plurals
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.TvShowIds
import org.koin.core.annotation.Factory

@Factory
internal class DetailsSeasonsUiModelMapper(
    private val getCurrentDateTime: GetCurrentDateTime
) {

    fun toUiModel(progress: TvShowProgress): DetailsSeasonsUiModel {
        val nonSpecialSeasons = progress.seasonsProgress.filterNot(::isSpecial)
        val seasonCount = nonSpecialSeasons.size
        val tvShowIds = progress.screenplay.ids
        val watchedSeasonCount = nonSpecialSeasons.count { it is SeasonProgress.Completed }
        return DetailsSeasonsUiModel(
            progress = Percent.of(watchedSeasonCount, seasonCount).toFloat(),
            seasonUiModels = progress.seasonsProgress.map { toUiModel(tvShowIds, it) },
            totalSeasons = TextRes.plural(plurals.details_total_seasons, seasonCount, seasonCount),
            tvShowIds = tvShowIds,
            watchedSeasons = TextRes.plural(plurals.details_watched, watchedSeasonCount, watchedSeasonCount)
        )
    }

    private fun toUiModel(tvShowIds: TvShowIds, seasonProgress: SeasonProgress): DetailsSeasonUiModel {
        val episodeCount = seasonProgress.episodesProgress.size
        val watchedEpisodeCount = seasonProgress.episodesProgress.count { it is EpisodeProgress.Watched }
        val episodeUiModels = seasonProgress.episodesProgress
            .map { toUiModel(tvShowIds, seasonProgress.season.number, it) }
        return DetailsSeasonUiModel(
            allReleased = episodeUiModels.all(DetailsEpisodeUiModel::released),
            completed = seasonProgress is SeasonProgress.Completed,
            episodeUiModels = episodeUiModels,
            progress = Percent.of(watchedEpisodeCount, episodeCount).toFloat(),
            seasonIds = seasonProgress.season.ids,
            title = seasonProgress.season.title,
            totalEpisodes = TextRes.plural(plurals.details_total_episodes, episodeCount, episodeCount),
            tvShowIds = tvShowIds,
            watchedEpisodes = TextRes.plural(plurals.details_watched, watchedEpisodeCount, watchedEpisodeCount)
        )
    }

    private fun toUiModel(
        tvShowIds: TvShowIds,
        seasonNumber: SeasonNumber,
        episodeProgress: EpisodeProgress
    ) = DetailsEpisodeUiModel(
        episodeIds = episodeProgress.episode.ids,
        episodeNumber = TextRes(string.details_episode_number, episodeProgress.episode.number.value),
        released = episodeProgress.episode.firstAirDate.fold(
            ifEmpty = { false },
            ifSome = { it <= getCurrentDateTime().date }
        ),
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(seasonNumber, episodeProgress.episode.number),
        title = episodeProgress.episode.title,
        tvShowIds = tvShowIds,
        watched = episodeProgress is EpisodeProgress.Watched
    )

    private fun isSpecial(progress: SeasonProgress) = progress.season.number.value == 0
}
