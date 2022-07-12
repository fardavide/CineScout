package cinescout.movies.data.local.mapper

import arrow.core.left
import arrow.core.right
import cinescout.database.FindAllWithRating
import cinescout.database.model.DatabaseMovie
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
        val expected = movie.right()
        val databaseMovie = DatabaseMovie(
            title = movie.title,
            tmdbId = movie.tmdbId.toDatabaseId()
        )

        // when
        val result = mapper.toMovie(databaseMovie)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps null movie`() {
        // given
        val expected = DataError.Local.NoCache.left()

        // when
        val result = mapper.toMovie(null)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps movies with rating`() = runTest {
        // given
        val expected = listOf(
            MovieWithRatingTestData.Inception,
            MovieWithRatingTestData.TheWolfOfWallStreet
        ).right()
        val databaseMoviesWithRating = listOf(
            FindAllWithRating(
                title = MovieWithRatingTestData.Inception.movie.title,
                tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId.toDatabaseId(),
                rating = MovieWithRatingTestData.Inception.rating.toDatabaseRating()
            ),
            FindAllWithRating(
                title = MovieWithRatingTestData.TheWolfOfWallStreet.movie.title,
                tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId.toDatabaseId(),
                rating = MovieWithRatingTestData.TheWolfOfWallStreet.rating.toDatabaseRating()
            )
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
            FindAllWithRating(
                title = MovieWithRatingTestData.Inception.movie.title,
                tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId.toDatabaseId(),
                rating = MovieWithRatingTestData.Inception.rating.toDatabaseRating()
            ),
            FindAllWithRating(
                title = MovieWithRatingTestData.TheWolfOfWallStreet.movie.title,
                tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId.toDatabaseId(),
                rating = 12
            )
        )

        // then
        assertFailsWith<IllegalStateException> {

            // when
            mapper.toMoviesWithRating(databaseMoviesWithRating)
        }
    }
}
