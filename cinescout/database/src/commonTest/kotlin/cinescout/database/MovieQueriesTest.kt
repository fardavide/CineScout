package cinescout.database

import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieQueriesTest : DatabaseTest() {

    private val movieQueries = database.movieQueries
    private val movieRatingQueries = database.movieRatingQueries

    @Test
    fun `insert and find movies`() {
        // given
        val movie = DatabaseMovieTestData.Inception

        // when
        movieQueries.insertMovie(tmdbId = movie.tmdbId, title = movie.title)
        val result = movieQueries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(movie, result)
    }

    @Test
    fun `insert and find all movie ratings`() {
        // given
        val movie = DatabaseMovieTestData.Inception
        val rating = 8
        val expected = listOf(
            FindAllWithRating(
                tmdbId = movie.tmdbId,
                title = movie.title,
                rating = rating
            )
        )

        // when
        movieQueries.insertMovie(tmdbId = movie.tmdbId, title = movie.title)
        movieRatingQueries.insertRating(tmdbId = movie.tmdbId, rating = rating)
        val result = movieQueries.findAllWithRating().executeAsList()

        // then
        assertEquals(expected, result)
    }
}