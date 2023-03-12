package cinescout.database

import cinescout.database.model.DatabaseWatchlist
import cinescout.database.sample.DatabaseTmdbScreenplayIdSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class WatchlistQueriesTest : DatabaseTest() {

    private val queries get() = database.watchlistQueries

    @Test
    fun insertAndFindWatchlist() {
        // given
        val movieId = DatabaseTmdbScreenplayIdSample.Inception
        val expected = DatabaseWatchlist(tmdbId = movieId)

        // when
        queries.insertWatchlist(tmdbId = movieId)
        val result = queries.findById(movieId).executeAsOneOrNull()

        // then
        assertEquals(expected.tmdbId, result)
    }

    @Test
    fun findMovieNotInWatchlist() {
        // given
        val movieId = DatabaseTmdbScreenplayIdSample.Inception

        // when
        val result = queries.findById(movieId).executeAsOneOrNull()

        // then
        assertNull(result)
    }
}
