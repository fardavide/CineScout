package cinescout.database

import cinescout.database.model.DatabaseWatchlist
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WatchlistQueriesTest : DatabaseTest() {

    private val queries = database.watchlistQueries

    @Test
    fun insertAndFindWatchlist() {
        // given
        val movie = DatabaseMovieTestData.Inception
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
        val movie = DatabaseMovieTestData.Inception

        // when
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertNull(result)
    }
}
