package cinescout.details.presentation.mapper

import cinescout.details.presentation.sample.MovieDetailsUiModelSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.movies.domain.testdata.MovieMediaTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MovieDetailsUiModelMapperTest {

    private val mapper = MovieDetailsUiModelMapper()

    @Test
    fun `maps correctly`() {
        // given
        val movie = MovieWithExtrasSample.Inception
        val media = MovieMediaTestData.Inception
        val expected = MovieDetailsUiModelSample.Inception

        // when
        val result = mapper.toUiModel(movie, media)

        // then
        assertEquals(expected, result)
    }
}
