package cinescout.details.presentation.sample

import arrow.core.Option
import cinescout.details.presentation.model.DetailsEpisodeUiModel
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.seasons.domain.sample.EpisodeSample
import cinescout.utils.kotlin.formatLocalDate
import korlibs.time.Date
import java.time.format.FormatStyle

object DetailsEpisodeUiModelSample {

    val BreakingBad_s0e1_Unwatched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s0e1.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e1.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s0e1.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s0e1.seasonNumber,
            episode = EpisodeSample.BreakingBad_s0e1.number
        ),
        title = EpisodeSample.BreakingBad_s0e1.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = false
    )

    val BreakingBad_s0e2_Unwatched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s0e2.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e2.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s0e2.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s0e2.seasonNumber,
            episode = EpisodeSample.BreakingBad_s0e2.number
        ),
        title = EpisodeSample.BreakingBad_s0e2.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = false
    )

    val BreakingBad_s0e3_Unwatched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s0e3.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s0e3.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s0e3.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s0e3.seasonNumber,
            episode = EpisodeSample.BreakingBad_s0e3.number
        ),
        title = EpisodeSample.BreakingBad_s0e3.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = false
    )

    val BreakingBad_s1e1_Watched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s1e1.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e1.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s1e1.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s1e1.seasonNumber,
            episode = EpisodeSample.BreakingBad_s1e1.number
        ),
        title = EpisodeSample.BreakingBad_s1e1.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = true
    )

    val BreakingBad_s1e2_Watched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s1e2.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e2.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s1e2.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s1e2.seasonNumber,
            episode = EpisodeSample.BreakingBad_s1e2.number
        ),
        title = EpisodeSample.BreakingBad_s1e2.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = true
    )

    val BreakingBad_s1e3_Watched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s1e3.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s1e3.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s1e3.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s1e3.seasonNumber,
            episode = EpisodeSample.BreakingBad_s1e3.number
        ),
        title = EpisodeSample.BreakingBad_s1e3.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = true
    )

    val BreakingBad_s2e1_Watched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s2e1.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e1.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s2e1.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s2e1.seasonNumber,
            episode = EpisodeSample.BreakingBad_s2e1.number
        ),
        title = EpisodeSample.BreakingBad_s2e1.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = true
    )

    val BreakingBad_s2e2_Unwatched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s2e2.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e2.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s2e2.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s2e2.seasonNumber,
            episode = EpisodeSample.BreakingBad_s2e2.number
        ),
        title = EpisodeSample.BreakingBad_s2e2.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = false
    )

    val BreakingBad_s2e3_Unwatched = DetailsEpisodeUiModel(
        episodeIds = EpisodeSample.BreakingBad_s2e3.ids,
        episodeNumber = TextRes(string.details_episode_number, EpisodeSample.BreakingBad_s2e3.number.value),
        firstAirDate = firstAirDate(EpisodeSample.BreakingBad_s2e3.firstAirDate),
        released = true,
        seasonAndEpisodeNumber = SeasonAndEpisodeNumber(
            season = EpisodeSample.BreakingBad_s2e3.seasonNumber,
            episode = EpisodeSample.BreakingBad_s2e3.number
        ),
        title = EpisodeSample.BreakingBad_s2e3.title,
        tvShowIds = ScreenplayIdsSample.BreakingBad,
        watched = false
    )

    private fun firstAirDate(optionDate: Option<Date>) = optionDate.fold(
        ifEmpty = { "" },
        ifSome = { it.formatLocalDate(FormatStyle.LONG) }
    )
}
