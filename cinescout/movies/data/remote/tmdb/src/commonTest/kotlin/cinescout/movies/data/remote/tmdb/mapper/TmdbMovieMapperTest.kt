package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.domain.sample.MovieSample
import cinescout.screenplay.data.mapper.ScreenplayMapper
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieMapperTest {

    private val mapper = TmdbMovieMapper(ScreenplayMapper())

    @Test
    fun `maps correctly single movie`() {
        // given
        val input = TmdbMovieSample.Inception
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
        val expected = listOf(ScreenplayWithPersonalRatingSample.Inception)

        // when
        val result = mapper.toMoviesWithRating(input)

        // then
        assertEquals(expected, result)
    }
}
