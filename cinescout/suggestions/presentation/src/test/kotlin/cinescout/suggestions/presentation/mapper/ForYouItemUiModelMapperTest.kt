package cinescout.suggestions.presentation.mapper

import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import kotlin.test.Test
import kotlin.test.assertEquals

class ForYouItemUiModelMapperTest {

    private val mapper = ForYouItemUiModelMapper()

    @Test
    fun `maps to ui model`() {
        // given
        val movie = MovieWithExtrasTestData.Inception
        val expected = ForYouMovieUiModelSample.Inception

        // when
        val result = mapper.toUiModel(movie)

        // then
        assertEquals(expected, result)
    }
}
