package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.domain.sample.MovieCreditsSample
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbMovieCreditsMapperTest {

    private val mapper = TmdbMovieCreditsMapper()

    @Test
    fun `maps correctly single credits`() {
        // given
        val input = GetMovieCreditsResponseTestData.TheWolfOfWallStreet
        val expected = MovieCreditsSample.TheWolfOfWallStreet

        // when
        val result = mapper.toMovieCredits(input)

        // then
        assertEquals(expected, result)
    }
}
