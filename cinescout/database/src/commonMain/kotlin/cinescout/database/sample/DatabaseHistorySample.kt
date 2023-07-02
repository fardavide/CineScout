package cinescout.database.sample

import cinescout.database.model.DatabaseHistory
import cinescout.database.model.id.DatabaseHistoryItemId
import cinescout.sample.DateTimeSample

object DatabaseHistorySample {

    val BreakingBad_s1e1 = DatabaseHistory(
        episodeNumber = 1,
        isPlaceholder = 0,
        itemId = DatabaseHistoryItemId(0),
        seasonNumber = 1,
        tmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad,
        traktId = DatabaseTraktScreenplayIdSample.BreakingBad,
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e2 = DatabaseHistory(
        episodeNumber = 2,
        isPlaceholder = 0,
        itemId = DatabaseHistoryItemId(1),
        seasonNumber = 1,
        tmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad,
        traktId = DatabaseTraktScreenplayIdSample.BreakingBad,
        watchedAt = DateTimeSample.Xmas2023
    )

    val Grimm_s1e1 = DatabaseHistory(
        episodeNumber = 1,
        isPlaceholder = 0,
        itemId = DatabaseHistoryItemId(2),
        seasonNumber = 1,
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        traktId = DatabaseTraktScreenplayIdSample.Grimm,
        watchedAt = DateTimeSample.Xmas2023
    )

    val Grimm_s1e2 = DatabaseHistory(
        episodeNumber = 2,
        isPlaceholder = 0,
        itemId = DatabaseHistoryItemId(3),
        seasonNumber = 1,
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        traktId = DatabaseTraktScreenplayIdSample.Grimm,
        watchedAt = DateTimeSample.Xmas2023
    )

    val Grimm_s1e3 = DatabaseHistory(
        episodeNumber = 3,
        isPlaceholder = 0,
        itemId = DatabaseHistoryItemId(4),
        seasonNumber = 1,
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        traktId = DatabaseTraktScreenplayIdSample.Grimm,
        watchedAt = DateTimeSample.Xmas2023
    )
}
