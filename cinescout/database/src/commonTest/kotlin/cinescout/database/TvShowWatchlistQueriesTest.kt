package cinescout.database

import cinescout.database.model.DatabaseTvShowWatchlist
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class TvShowWatchlistQueriesTest : DatabaseTest() {

    private val queries get() = database.tvShowWatchlistQueries

    @Test
    fun insertAndFindWatchlist() {
        // given
        val tvShow = DatabaseTvShowSample.Grimm
        val expected = DatabaseTvShowWatchlist(tmdbId = tvShow.tmdbId, isInWatchlist = true)

        // when
        queries.insertWatchlist(tmdbId = tvShow.tmdbId)
        val result = queries.findById(tvShow.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun findTvShowNotInWatchlist() {
        // given
        val tvShow = DatabaseTvShowSample.Grimm

        // when
        val result = queries.findById(tvShow.tmdbId).executeAsOneOrNull()

        // then
        assertNull(result)
    }
}
