package cinescout.suggestions.presentation.mapper

import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouMovieUiModelMapperTest {

    private val mapper = ForYouMovieUiModelMapper()

    @Test
    fun `maps to ui model`() {
        // given
        val movie = MovieTestData.Inception
        val credits = MovieCreditsTestData.Inception
        val expected = ForYouMovieUiModelPreviewData.Inception

        // when
        val result = mapper.toUiModel(movie, credits)

        // then
        assertEquals(expected, result)
    }
}
