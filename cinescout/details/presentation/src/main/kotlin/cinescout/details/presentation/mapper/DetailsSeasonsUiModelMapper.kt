package cinescout.details.presentation.mapper

import cinescout.details.presentation.model.DetailsEpisodeUiModel
import cinescout.details.presentation.model.DetailsSeasonUiModel
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.model.Percent
import cinescout.progress.domain.model.EpisodeProgress
import cinescout.progress.domain.model.SeasonProgress
import cinescout.progress.domain.model.TvShowProgress
import cinescout.resources.R.plurals
import cinescout.resources.TextRes
import org.koin.core.annotation.Factory

@Factory
internal class DetailsSeasonsUiModelMapper {

    fun toUiModel(progress: TvShowProgress): DetailsSeasonsUiModel {
        val nonSpecialSeasons = progress.seasonsProgress.filterNot(::isSpecial)
        val seasonCount = nonSpecialSeasons.size
        val watchedSeasonCount = nonSpecialSeasons.count { it is SeasonProgress.Completed }
        return DetailsSeasonsUiModel(
            progress = Percent.of(watchedSeasonCount, seasonCount).toFloat(),
            seasonUiModels = progress.seasonsProgress.map(::toUiModel),
            totalSeasons = TextRes.plural(plurals.details_total_seasons, seasonCount, seasonCount),
            watchedSeasons = TextRes.plural(plurals.details_watched, watchedSeasonCount, watchedSeasonCount)
        )
    }

    private fun toUiModel(seasonProgress: SeasonProgress): DetailsSeasonUiModel {
        val episodeCount = seasonProgress.episodesProgress.size
        val watchedEpisodeCount = seasonProgress.episodesProgress.count { it is EpisodeProgress.Watched }
        return DetailsSeasonUiModel(
            episodeUiModels = seasonProgress.episodesProgress.map(::toUiModel),
            progress = Percent.of(watchedEpisodeCount, episodeCount).toFloat(),
            title = seasonProgress.season.title,
            totalEpisodes = TextRes.plural(plurals.details_total_episodes, episodeCount, episodeCount),
            watchedEpisodes = TextRes.plural(plurals.details_watched, watchedEpisodeCount, watchedEpisodeCount)
        )
    }

    private fun toUiModel(episodeProgress: EpisodeProgress) = DetailsEpisodeUiModel(
        title = episodeProgress.episode.title,
        watched = episodeProgress is EpisodeProgress.Watched
    )

    private fun isSpecial(progress: SeasonProgress) = progress.season.number.value == 0
}
