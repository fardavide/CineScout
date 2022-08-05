package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database

object TestDatabase {

    fun createDriver() = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    fun createDatabase(driver: JdbcSqliteDriver) = with(TestAdapters) {
        Database(
            driver = driver,
            movieAdapter = MovieAdapter,
            movieCastMemberAdapter = MovieCastMemberAdapter,
            movieCrewMemberAdapter = MovieCrewMemberAdapter,
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
