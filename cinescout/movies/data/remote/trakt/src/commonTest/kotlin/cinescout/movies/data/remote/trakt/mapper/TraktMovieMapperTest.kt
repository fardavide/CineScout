package cinescout.movies.data.remote.trakt.mapper

import cinescout.movies.data.remote.testdata.TraktMovieRatingTestData
import cinescout.movies.data.remote.trakt.testdata.GetRatingsTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TraktMovieMapperTest {

    private val mapper = TraktMovieMapper()

    @Test
    fun `maps correctly movie rating`() {
        // given
        val input = GetRatingsTestData.Inception
        val expected = TraktMovieRatingTestData.Inception

        // when
        val result = mapper.toMovieRating(input)

        // then
        assertEquals(expected, result)
    }
}
