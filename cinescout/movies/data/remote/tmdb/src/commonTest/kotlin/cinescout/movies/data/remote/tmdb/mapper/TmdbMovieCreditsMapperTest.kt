package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbMovieCreditsMapperTest {

    private val mapper = TmdbMovieCreditsMapper()

    @Test
    fun `maps correctly single credits`() {
        // given
        val input = GetMovieCreditsResponseTestData.Inception
        val expected = MovieCreditsTestData.Inception

        // when
        val result = mapper.toMovieCredits(input)

        // then
        assertEquals(expected, result)
    }
}
