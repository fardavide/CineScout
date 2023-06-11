package cinescout.progress.domain.model

import arrow.core.Nel
import cinescout.model.Percent
import cinescout.screenplay.domain.model.TvShow
import cinescout.seasons.domain.model.Episode
import cinescout.seasons.domain.model.Season

sealed interface TvShowProgress : ScreenplayProgress {

    override val screenplay: TvShow
    val seasonsProgress: Nel<SeasonProgress>

    data class Completed(
        override val screenplay: TvShow,
        override val seasonsProgress: Nel<SeasonProgress>
    ) : TvShowProgress

    data class InProgress(
        val progress: Percent,
        override val screenplay: TvShow,
        override val seasonsProgress: Nel<SeasonProgress>
    ) : TvShowProgress

    data class Unwatched(
        override val screenplay: TvShow,
        override val seasonsProgress: Nel<SeasonProgress.Unwatched>
    ) : TvShowProgress

    data class WaitingForNextEpisode(
        override val screenplay: TvShow,
        override val seasonsProgress: Nel<SeasonProgress>
    ) : TvShowProgress
}

sealed interface SeasonProgress {

    val episodesProgress: List<EpisodeProgress>
    val season: Season

    data class Completed(
        override val episodesProgress: List<EpisodeProgress.Watched>,
        override val season: Season
    ) : SeasonProgress

    data class InProgress(
        override val episodesProgress: List<EpisodeProgress>,
        val progress: Percent,
        override val season: Season
    ) : SeasonProgress

    data class Unwatched(
        override val episodesProgress: List<EpisodeProgress.Unwatched>,
        override val season: Season
    ) : SeasonProgress

    data class WaitingForNextEpisode(
        override val episodesProgress: List<EpisodeProgress>,
        override val season: Season
    ) : SeasonProgress
}

sealed interface EpisodeProgress {

    val episode: Episode

    data class Unwatched(override val episode: Episode) : EpisodeProgress

    data class Watched(
        val count: Int,
        override val episode: Episode
    ) : EpisodeProgress
}
