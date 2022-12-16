package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieMapperTest {

    private val mapper = TmdbMovieMapper()

    @Test
    fun `maps correctly single movie`() {
        // given
        val input = TmdbMovieTestData.Inception
        val expected = MovieSample.Inception

        // when
        val result = mapper.toMovie(input)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps correctly movies with rating`() {
        // given
        val input = GetRatedMoviesResponseTestData.OneMovie
        val expected = listOf(MovieWithPersonalRatingTestData.Inception)

        // when
        val result = mapper.toMoviesWithRating(input)

        // then
        assertEquals(expected, result)
    }
}
