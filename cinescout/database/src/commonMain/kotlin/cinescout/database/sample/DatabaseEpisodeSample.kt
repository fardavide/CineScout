package cinescout.database.sample

import cinescout.database.model.DatabaseEpisode
import cinescout.database.model.id.DatabaseTmdbEpisodeId
import cinescout.database.model.id.DatabaseTraktEpisodeId
import korlibs.time.Date
import kotlin.time.Duration.Companion.minutes

object DatabaseEpisodeSample {

    val BreakingBad_s0e1 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 1,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s0.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s0.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(0),
        traktId = DatabaseTraktEpisodeId(0)
    )

    val BreakingBad_s0e2 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 2,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s0.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s0.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(1),
        traktId = DatabaseTraktEpisodeId(1)
    )

    val BreakingBad_s0e3 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 3,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s0.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s0.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(2),
        traktId = DatabaseTraktEpisodeId(2)
    )

    val BreakingBad_s1e1 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 1,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s1.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s1.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(3),
        traktId = DatabaseTraktEpisodeId(3)
    )

    val BreakingBad_s1e2 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 2,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s1.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s1.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(4),
        traktId = DatabaseTraktEpisodeId(4)
    )

    val BreakingBad_s1e3 = DatabaseEpisode(
        firstAirDate = Date(2008, 1, 20),
        number = 3,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s1.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s1.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(5),
        traktId = DatabaseTraktEpisodeId(5)
    )

    val BreakingBad_s2e1 = DatabaseEpisode(
        firstAirDate = Date(2009, 1, 20),
        number = 1,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s2.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s2.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(6),
        traktId = DatabaseTraktEpisodeId(6)
    )

    val BreakingBad_s2e2 = DatabaseEpisode(
        firstAirDate = Date(2009, 1, 20),
        number = 2,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s2.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s2.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(7),
        traktId = DatabaseTraktEpisodeId(7)
    )

    val BreakingBad_s2e3 = DatabaseEpisode(
        firstAirDate = Date(2009, 1, 20),
        number = 3,
        overview = "",
        ratingAverage = 0.0,
        ratingCount = 0,
        runtime = 0.minutes,
        seasonId = DatabaseSeasonSample.BreakingBad_s2.traktId,
        seasonNumber = DatabaseSeasonSample.BreakingBad_s2.number,
        title = "",
        tmdbId = DatabaseTmdbEpisodeId(8),
        traktId = DatabaseTraktEpisodeId(8)
    )
}
