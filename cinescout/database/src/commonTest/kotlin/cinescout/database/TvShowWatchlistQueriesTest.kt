package cinescout.database

import cinescout.database.model.DatabaseTvShowWatchlist
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TvShowWatchlistQueriesTest : DatabaseTest() {

    private val queries = database.tvShowWatchlistQueries

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
