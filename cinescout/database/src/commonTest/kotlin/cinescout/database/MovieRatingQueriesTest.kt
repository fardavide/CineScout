package cinescout.database

import cinescout.database.model.DatabaseMovieRating
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieRatingQueriesTest : DatabaseTest() {

    private val queries = database.movieRatingQueries

    @Test
    fun insertAndFindRating() {
        // given
        val movie = DatabaseMovieSample.Inception
        val rating = 8.0
        val expected = DatabaseMovieRating(tmdbId = movie.tmdbId, rating = rating)

        // when
        queries.insertRating(tmdbId = movie.tmdbId, rating = rating)
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun findMovieNotRated() {
        // given
        val movie = DatabaseMovieSample.Inception

        // when
        val result = queries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertNull(result)
    }
}
