package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.domain.testdata.MovieTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieMapperTest {

    private val mapper = TmdbMovieMapper()

    @Test
    fun `maps correctly`() {
        // given
        val input = TmdbMovieTestData.Inception
        val expected = MovieTestData.Inception

        // when
        val result = mapper.toMovie(input)

        // then
        assertEquals(expected, result)
    }
}
