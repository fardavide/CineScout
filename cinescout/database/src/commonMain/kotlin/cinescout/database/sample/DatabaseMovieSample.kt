package cinescout.database.sample

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbMovieId
import com.soywiz.klock.Date

object DatabaseMovieSample {

    val Inception = DatabaseMovie(
        backdropPath = "s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        posterPath = "8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg",
        ratingAverage = 8.357,
        ratingCount = 31_990,
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tmdbId = DatabaseTmdbMovieId(27_205)
    )

    val TheWolfOfWallStreet = DatabaseMovie(
        backdropPath = "cWUOv3H7YFwvKeaQhoAQTLLpo9Z.jpg",
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        posterPath = "jTlIYjvS16XOpsfvYCTmtEHV10K.jpg",
        ratingAverage = 8.031,
        ratingCount = 20_121,
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tmdbId = DatabaseTmdbMovieId(106_646)
    )

    val War = DatabaseMovie(
        backdropPath = "5Tw0isY4Fs08burneYsa6JvHbER.jpg",
        overview = "A biographical drama based on the life of Haseena Parkar, the sister of underworld don Dawood " +
            "Ibrahim.",
        posterPath = "7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg",
        ratingAverage = 6.8,
        ratingCount = 166,
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tmdbId = DatabaseTmdbMovieId(585_268)
    )
}
