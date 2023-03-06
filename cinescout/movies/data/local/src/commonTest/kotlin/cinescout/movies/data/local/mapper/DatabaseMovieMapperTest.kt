package cinescout.movies.data.local.mapper

import arrow.core.nonEmptyListOf
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.testdata.DatabaseMovieWithRatingTestData
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows

internal class DatabaseMovieMapperTest : AnnotationSpec() {

    private val mapper = DatabaseMovieMapper()

    @Test
    fun `maps single movie`() {
        // given
        val movie = MovieSample.Inception
        val databaseMovie = DatabaseMovieSample.Inception

        // when
        val result = mapper.toMovie(databaseMovie)

        // then
        result shouldBe movie
    }

    @Test
    fun `maps movies with rating`() = runTest {
        // given
        val expected = nonEmptyListOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
        val databaseMoviesWithRating = nonEmptyListOf(
            DatabaseMovieWithRatingTestData.Inception,
            DatabaseMovieWithRatingTestData.TheWolfOfWallStreet
        )

        // when
        val result = mapper.toMoviesWithRating(databaseMoviesWithRating)

        // then
        result shouldBe expected
    }

    @Test
    fun `throws with invalid rating`() = runTest {
        // given
        val databaseMoviesWithRating = nonEmptyListOf(
            DatabaseMovieWithRatingTestData.Inception,
            DatabaseMovieWithRatingTestData.War.copy(personalRating = 12.0)
        )

        // then
        assertThrows<IllegalStateException> {

            // when
            mapper.toMoviesWithRating(databaseMoviesWithRating)
        }
    }
}
