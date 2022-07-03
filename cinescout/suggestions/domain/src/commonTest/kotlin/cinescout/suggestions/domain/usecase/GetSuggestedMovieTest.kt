package cinescout.suggestions.domain.usecase

import arrow.core.left
import cinescout.suggestions.domain.model.NoSuggestions
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetSuggestedMovieTest {

    private val getSuggestedMovie = GetSuggestedMovie()

    @Test
    fun `when no suggestions`() = runTest {
        assertEquals(NoSuggestions.left(), getSuggestedMovie())
    }
}
