package cinescout.details.presentation.mapper

import cinescout.details.presentation.previewdata.MovieDetailsUiModelPreviewData
import cinescout.movies.domain.testdata.MovieMediaTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MovieDetailsUiModelMapperTest {

    private val mapper = MovieDetailsUiModelMapper()

    @Test
    fun `maps correctly`() {
        // given
        val movie = MovieWithExtrasTestData.Inception
        val media = MovieMediaTestData.Inception
        val expected = MovieDetailsUiModelPreviewData.Inception

        // when
        val result = mapper.toUiModel(movie, media)

        // then
        assertEquals(expected, result)
    }
}
