package cinescout.movies.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MovieRatingTest {

    @Test
    fun `equals on similar`() {
        val first = MovieRating(voteCount = 8095, average = Rating.of(value = 7.438).getOrThrow())
        val second = MovieRating(voteCount = 8094, average = Rating.of(value = 7.4).getOrThrow())

        assertEquals(first, second)
    }

    @Test
    fun `equals on not similar`() {
        val first = MovieRating(voteCount = 8095, average = Rating.of(value = 7.438).getOrThrow())
        val second = MovieRating(voteCount = 8094, average = Rating.of(value = 7.5).getOrThrow())

        assertNotEquals(first, second)
    }
}
