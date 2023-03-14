package cinescout.database.sample

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbMovieId
import com.soywiz.klock.Date

object DatabaseMovieSample {

    val Inception = DatabaseMovie(
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        ratingAverage = 8.357,
        ratingCount = 31_990,
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tmdbId = DatabaseTmdbMovieId(27_205)
    )

    val TheWolfOfWallStreet = DatabaseMovie(
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        ratingAverage = 8.031,
        ratingCount = 20_121,
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tmdbId = DatabaseTmdbMovieId(106_646)
    )

    val War = DatabaseMovie(
        overview = "A biographical drama based on the life of Haseena Parkar, the sister of underworld don Dawood " +
            "Ibrahim.",
        ratingAverage = 6.8,
        ratingCount = 166,
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tmdbId = DatabaseTmdbMovieId(585_268)
    )
}
