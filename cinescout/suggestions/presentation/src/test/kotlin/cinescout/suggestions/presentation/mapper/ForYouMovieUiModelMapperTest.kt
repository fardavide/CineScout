package cinescout.suggestions.presentation.mapper

import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouMovieUiModelMapperTest {

    private val mapper = ForYouMovieUiModelMapper()

    @Test
    fun `maps to ui model`() {
        // given
        val movie = MovieWithExtrasTestData.Inception
        val expected = ForYouMovieUiModelPreviewData.Inception

        // when
        val result = mapper.toUiModel(movie)

        // then
        assertEquals(expected, result)
    }
}
