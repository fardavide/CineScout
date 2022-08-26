package cinescout.lists.presentation.mapper

import cinescout.lists.presentation.previewdata.WatchlistItemUiModelPreviewData
import cinescout.movies.domain.testdata.MovieTestData
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchlistItemUiModelMapperTest {

    private val mapper = WatchlistItemUiModelMapper()

    @Test
    fun `maps to ui model`() {
        // given
        val movie = MovieTestData.Inception
        val expected = WatchlistItemUiModelPreviewData.Inception

        // when
        val result = mapper.toUiModel(movie)

        // then
        assertEquals(expected, result)
    }
}
