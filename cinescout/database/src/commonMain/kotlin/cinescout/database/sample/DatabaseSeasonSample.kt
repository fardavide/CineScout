package cinescout.database.sample

import cinescout.database.model.DatabaseSeason
import cinescout.database.model.id.DatabaseTmdbSeasonId
import cinescout.database.model.id.DatabaseTraktSeasonId
import korlibs.time.Date

object DatabaseSeasonSample {

    val BreakingBad_s0 = DatabaseSeason(
        episodeCount = 0,
        firstAirDate = Date.Companion(year = 2008, month = 1, day = 20),
        number = 0,
        ratingAverage = 0.0,
        ratingCount = 0,
        title = "",
        tmdbId = DatabaseTmdbSeasonId(0),
        traktId = DatabaseTraktSeasonId(0),
        tvShowId = DatabaseTraktScreenplayIdSample.BreakingBad
    )

    val BreakingBad_s1 = DatabaseSeason(
        episodeCount = 7,
        firstAirDate = Date.Companion(year = 2008, month = 1, day = 20),
        number = 1,
        ratingAverage = 9.0,
        ratingCount = 1,
        title = "Season 1",
        tmdbId = DatabaseTmdbSeasonId(1),
        traktId = DatabaseTraktSeasonId(1),
        tvShowId = DatabaseTraktScreenplayIdSample.BreakingBad
    )

    val BreakingBad_s2 = DatabaseSeason(
        episodeCount = 13,
        firstAirDate = Date.Companion(year = 2009, month = 1, day = 20),
        number = 2,
        ratingAverage = 9.0,
        ratingCount = 1,
        title = "Season 2",
        tmdbId = DatabaseTmdbSeasonId(2),
        traktId = DatabaseTraktSeasonId(2),
        tvShowId = DatabaseTraktScreenplayIdSample.BreakingBad
    )
}
