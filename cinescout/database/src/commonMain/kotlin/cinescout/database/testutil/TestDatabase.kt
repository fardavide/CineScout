package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database

object TestDatabase {

    fun createDriver() = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    fun createDatabase(driver: JdbcSqliteDriver) = with(TestAdapters) {
        Database(
            driver = driver,
            genreAdapter = GenreAdapter,
            likedMovieAdapter = LikedMovieAdapter,
            movieAdapter = MovieAdapter,
            movieCastMemberAdapter = MovieCastMemberAdapter,
            movieCrewMemberAdapter = MovieCrewMemberAdapter,
            movieGenreAdapter = MovieGenreAdapter,
            movieRatingAdapter = MovieRatingAdapter,
            personAdapter = PersonAdapter,
            tmdbAccountAdapter = TmdbAccountAdapter,
            tmdbAuthStateAdapter = TmdbAuthStateAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
