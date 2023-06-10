package cinescout.history.domain.sample

import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber

object ScreenplayHistoryItemSample {

    val BreakingBad_s0e1 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(1),
        id = HistoryItemIdSample.BreakingBad_s0e1,
        seasonNumber = SeasonNumber(0),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s0e2 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(2),
        id = HistoryItemIdSample.BreakingBad_s0e2,
        seasonNumber = SeasonNumber(0),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s0e3 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(3),
        id = HistoryItemIdSample.BreakingBad_s0e3,
        seasonNumber = SeasonNumber(0),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e1 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(1),
        id = HistoryItemIdSample.BreakingBad_s1e1,
        seasonNumber = SeasonNumber(1),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e2 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(2),
        id = HistoryItemIdSample.BreakingBad_s1e2,
        seasonNumber = SeasonNumber(1),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e3 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(3),
        id = HistoryItemIdSample.BreakingBad_s1e3,
        seasonNumber = SeasonNumber(1),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s2e1 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(1),
        id = HistoryItemIdSample.BreakingBad_s2e1,
        seasonNumber = SeasonNumber(2),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s2e2 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(2),
        id = HistoryItemIdSample.BreakingBad_s2e2,
        seasonNumber = SeasonNumber(2),
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s2e3 = ScreenplayHistoryItem.Episode(
        episodeNumber = EpisodeNumber(3),
        id = HistoryItemIdSample.BreakingBad_s2e3,
        seasonNumber = SeasonNumber(2),
        watchedAt = DateTimeSample.Xmas2023
    )

    val Inception = ScreenplayHistoryItem.Movie(
        id = HistoryItemIdSample.Inception,
        watchedAt = DateTimeSample.Xmas2023
    )
}
