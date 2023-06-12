package cinescout.details.presentation.action

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.screenplay.domain.model.ids.TvShowIds

sealed interface ScreenplayDetailsAction {

    data class AddEpisodeToHistory(
        val tvShowIds: TvShowIds,
        val episodeIds: EpisodeIds,
        val episode: SeasonAndEpisodeNumber
    ) : ScreenplayDetailsAction

    data class AddMovieToHistory(val movieIds: MovieIds) : ScreenplayDetailsAction

    data class AddSeasonToHistory(
        val tvShowIds: TvShowIds,
        val seasonIds: SeasonIds,
        val episodes: List<SeasonAndEpisodeNumber>
    ) : ScreenplayDetailsAction

    data class AddTvShowToHistory(
        val tvShowIds: TvShowIds,
        val episodes: List<SeasonAndEpisodeNumber>
    ) : ScreenplayDetailsAction

    data class Rate(val rating: Rating) : ScreenplayDetailsAction

    object ToggleWatchlist : ScreenplayDetailsAction
}
