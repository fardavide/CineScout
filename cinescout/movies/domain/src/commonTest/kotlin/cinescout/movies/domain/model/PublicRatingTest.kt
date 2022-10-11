package cinescout.movies.domain.model

import cinescout.common.model.PublicRating
import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PublicRatingTest {

    @Test
    fun `equals on similar`() {
        val first = PublicRating(voteCount = 8095, average = Rating.of(value = 7.438).getOrThrow())
        val second = PublicRating(voteCount = 8094, average = Rating.of(value = 7.4).getOrThrow())

        assertEquals(first, second)
    }

    @Test
    fun `equals on not similar`() {
        val first = PublicRating(voteCount = 8095, average = Rating.of(value = 7.438).getOrThrow())
        val second = PublicRating(voteCount = 8094, average = Rating.of(value = 7.5).getOrThrow())

        assertNotEquals(first, second)
    }
}
