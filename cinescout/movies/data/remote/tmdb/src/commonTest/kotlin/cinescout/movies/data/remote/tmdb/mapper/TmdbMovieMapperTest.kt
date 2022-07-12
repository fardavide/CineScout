package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieMapperTest {

    private val mapper = TmdbMovieMapper()

    @Test
    fun `maps correctly single movie`() {
        // given
        val input = TmdbMovieTestData.Inception
        val expected = MovieTestData.Inception

        // when
        val result = mapper.toMovie(input)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `maps correctly movies with rating`() {
        // given
        val input = GetRatedMoviesResponseTestData.OneMovie
        val expected = listOf(MovieWithRatingTestData.Inception)

        // when
        val result = mapper.toMoviesWithRating(input)

        // then
        assertEquals(expected, result)
    }
}
