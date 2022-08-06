package cinescout.database.testdata

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbMovieId
import com.soywiz.klock.Date

object DatabaseMovieTestData {

    val Inception = DatabaseMovie(
        backdropPath = "ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg",
        posterPath = "8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg",
        ratingAverage = 8.357,
        ratingCount = 31_990,
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tmdbId = DatabaseTmdbMovieId(27_205)
    )

    val TheWolfOfWallStreet = DatabaseMovie(
        backdropPath = "blbA7NEHARQOWy5i9VF5K2kHrPc.jpg",
        posterPath = "pWHf4khOloNVfCxscsXFj3jj6gP.jpg",
        ratingAverage = 8.031,
        ratingCount = 20_121,
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tmdbId = DatabaseTmdbMovieId(106_646)
    )

    var War = DatabaseMovie(
        backdropPath = "5Tw0isY4Fs08burneYsa6JvHbER.jpg",
        posterPath = "7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg",
        ratingAverage = 6.8,
        ratingCount = 166,
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tmdbId = DatabaseTmdbMovieId(585_268)
    )
}
