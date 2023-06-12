package cinescout.details.presentation.sample

import cinescout.details.presentation.model.DetailsEpisodeUiModel
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.seasons.domain.sample.EpisodeSample

object DetailsEpisodeUiModelSample {

    val BreakingBad_s0e1_Unwatched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e1.number.value),
        title = EpisodeSample.BreakingBad_s0e1.title,
        watched = false
    )

    val BreakingBad_s0e2_Unwatched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e2.number.value),
        title = EpisodeSample.BreakingBad_s0e2.title,
        watched = false
    )

    val BreakingBad_s0e3_Unwatched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e3.number.value),
        title = EpisodeSample.BreakingBad_s0e3.title,
        watched = false
    )

    val BreakingBad_s1e1_Watched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e1.number.value),
        title = EpisodeSample.BreakingBad_s1e1.title,
        watched = true
    )

    val BreakingBad_s1e2_Watched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e2.number.value),
        title = EpisodeSample.BreakingBad_s1e2.title,
        watched = true
    )

    val BreakingBad_s1e3_Watched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e3.number.value),
        title = EpisodeSample.BreakingBad_s1e3.title,
        watched = true
    )

    val BreakingBad_s2e1_Watched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e1.number.value),
        title = EpisodeSample.BreakingBad_s2e1.title,
        watched = true
    )

    val BreakingBad_s2e2_Unwatched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e2.number.value),
        title = EpisodeSample.BreakingBad_s2e2.title,
        watched = false
    )

    val BreakingBad_s2e3_Unwatched = DetailsEpisodeUiModel(
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e3.number.value),
        title = EpisodeSample.BreakingBad_s2e3.title,
        watched = false
    )
}
