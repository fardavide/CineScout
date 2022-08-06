package cinescout.movies.data.local.mapper

import arrow.core.left
import arrow.core.right
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testdata.DatabaseMovieWithRatingTestData
import cinescout.error.DataError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class DatabaseMovieMapperTest {

    private val mapper = DatabaseMovieMapper()

    @Test
    fun `maps single movie`() {
        // given
        val movie = MovieTestData.Inception
        val databaseMovie = DatabaseMovieTestData.Inception

        // when
        val result = mapper.toMovie(databaseMovie)

        // then
        assertEquals(movie, result)
    }

    @Test
    fun `maps movies with rating`() = runTest {
        // given
        val expected = listOf(
            MovieWithRatingTestData.Inception,
            MovieWithRatingTestData.TheWolfOfWallStreet
        ).right()
        val databaseMoviesWithRating = listOf(
            DatabaseMovieWithRatingTestData.Inception,
            DatabaseMovieWithRatingTestData.TheWolfOfWallStreet
        )

        // when
        val result = mapper.toMoviesWithRating(databaseMoviesWithRating)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps empty list of movies with rating`() = runTest {
        // given
        val expected = DataError.Local.NoCache.left()

        // when
        val result = mapper.toMoviesWithRating(emptyList())

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps with invalid rating`() = runTest {
        // given
        val databaseMoviesWithRating = listOf(
            DatabaseMovieWithRatingTestData.Inception,
            DatabaseMovieWithRatingTestData.War.copy(personalRating = 12.0)
        )

        // then
        assertFailsWith<IllegalStateException> {

            // when
            mapper.toMoviesWithRating(databaseMoviesWithRating)
        }
    }
}
