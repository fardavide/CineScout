package cinescout.details.presentation.mapper

import cinescout.details.presentation.previewdata.MovieDetailsUiModelPreviewData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MovieDetailsUiModelMapperTest {

    private val mapper = MovieDetailsUiModelMapper()

    @Test
    fun `maps correctly`() {
        // given
        val movie = MovieWithExtrasTestData.Inception
        val expected = MovieDetailsUiModelPreviewData.Inception

        // when
        val result = mapper.toUiModel(movie)

        // then
        assertEquals(expected, result)
    }
}
