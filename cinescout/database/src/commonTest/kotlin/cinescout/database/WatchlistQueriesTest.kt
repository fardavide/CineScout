package cinescout.database

import cinescout.database.model.DatabaseWatchlist
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class WatchlistQueriesTest : DatabaseTest() {

    private val queries get() = database.watchlistQueries

    @Test
    fun insertAndFindWatchlist() {
        // given
        val movie = DatabaseMovieSample.Inception
        val expected = DatabaseWatchlist(tmdbId = movie.tmdbId, isInWatchlist = true)

        // when
        queries.insertWatchlist(tmdbId = movie.tmdbId)
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun findMovieNotInWatchlist() {
        // given
        val movie = DatabaseMovieSample.Inception

        // when
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertNull(result)
    }
}
