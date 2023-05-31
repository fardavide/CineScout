package cinescout.history.domain.sample

import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber

object ScreenplayHistoryItemSample {

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
}
