package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplay
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import com.soywiz.klock.Date

object DatabaseScreenplaySample {

    val BreakingBad = DatabaseScreenplay(
        firstAirDate = Date(year = 2008, month = 1, day = 20),
        movieTmdbId = null,
        overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire " +
            "medical diagnosis. Street-savvy former student Jesse Pinkman teaches Walter a new trade.",
        ratingAverage = 8.839,
        ratingCount = 10_125,
        releaseDate = null,
        title = "Breaking Bad",
        tvShowTmdbId = DatabaseTmdbTvShowId(1_396)
    )

    val Grimm = DatabaseScreenplay(
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        movieTmdbId = null,
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as \"Grimms,\" he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        ratingAverage = 8.259,
        ratingCount = 2_613,
        releaseDate = null,
        title = "Grimm",
        tvShowTmdbId = DatabaseTmdbTvShowId(39_351)
    )

    val Inception = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseTmdbMovieId(27_205),
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        ratingAverage = 8.357,
        ratingCount = 31_990,
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tvShowTmdbId = null
    )

    val TheWolfOfWallStreet = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseTmdbMovieId(106_646),
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        ratingAverage = 8.031,
        ratingCount = 20_121,
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tvShowTmdbId = null
    )

    val War = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseTmdbMovieId(585_268),
        overview = "A biographical drama based on the life of Haseena Parkar, the sister of underworld don Dawood " +
            "Ibrahim.",
        ratingAverage = 6.8,
        ratingCount = 166,
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tvShowTmdbId = null
    )
}
