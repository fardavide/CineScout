package cinescout.database

import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieQueriesTest : DatabaseTest() {

    private val queries = database.movieQueries

    @Test
    fun `insert and find movies`() {
        // given
        val movie = DatabaseMovieTestData.Inception

        // when
        queries.insertMovie(tmdbId = movie.tmdbId, title = movie.title)
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(movie, result)
    }
}
