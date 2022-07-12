package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.Movie
import com.soywiz.klock.Date

object MovieTestData {

    val Inception = Movie(
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tmdbId = TmdbMovieIdTestData.Inception
    )

    val TheWolfOfWallStreet = Movie(
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tmdbId = TmdbMovieIdTestData.TheWolfOfWallStreet
    )

    val War = Movie(
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tmdbId = TmdbMovieIdTestData.War
    )
}
