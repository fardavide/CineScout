package cinescout.database.sample

import cinescout.database.model.DatabaseTvShow
import korlibs.time.Date
import korlibs.time.DateTime

object DatabaseTvShowSample {

    val BreakingBad = DatabaseTvShow(
        firstAirDate = Date(year = 2008, month = 1, day = 20),
        overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire " +
            "medical diagnosis. Street-savvy former student Jesse Pinkman teaches Walter a new trade.",
        ratingAverage = 8.839,
        ratingCount = 10_125,
        title = "Breaking Bad",
        tmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad,
        traktId = DatabaseTraktScreenplayIdSample.BreakingBad
    )

    val Grimm = DatabaseTvShow(
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as \"Grimms,\" he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        ratingAverage = 8.259,
        ratingCount = 2_613,
        title = "Grimm",
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        traktId = DatabaseTraktScreenplayIdSample.Grimm
    )

    @Suppress("StringShouldBeRawString")
    val TVPatrolNorthernLuzon = DatabaseTvShow(
        firstAirDate = DateTime.EPOCH.date,
        overview = "TV Patrol Northern Luzon is a local news program of ABS-CBN Regional Network Group in " +
            "Northern Luzon, shown in the northern Luzon cities and provinces of Baguio, entire Cordillera, " +
            "La Union, Ilocos Sur and Parts of Pangasinan are San Fabian, Sison and San Manuel.\n\nThe " +
            "newscast is done in a tabloid-style format. It delivers the most recent news and current affairs " +
            "issues in Northern Luzon in the Tagalog language.\n\nIt is aired live daily from the ABS-CBN " +
            "Baguio at 5:00 PM, from Monday to Friday, simulcast also on Channel 30 in La Union and Channel 11 " +
            "Mountain Province. It is also simulcast on radio via MOR 103.1 Baguio For Life!\n\nTV Patrol Northern " +
            "Luzon is anchored by Dobie de Guzman and Maira Wallis. The program is backed by a team of ABS-CBN " +
            "reporters.",
        ratingAverage = 0.0,
        ratingCount = 0,
        title = "TV Patrol Northern Luzon",
        tmdbId = DatabaseTmdbScreenplayIdSample.TVPatrolNorthernLuzon,
        traktId = DatabaseTraktScreenplayIdSample.TVPatrolNorthernLuzon
    )
}
