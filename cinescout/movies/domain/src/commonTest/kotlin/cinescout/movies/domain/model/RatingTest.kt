package cinescout.movies.domain.model

import arrow.core.Invalid
import arrow.core.Valid
import kotlin.test.Test
import kotlin.test.assertIs

internal class RatingTest {

    @Test
    fun `zero is valid`() {
        val result = Rating.of(0)
        assertIs<Valid<Rating>>(result)
    }

    @Test
    fun `ten is valid`() {
        val result = Rating.of(10)
        assertIs<Valid<Rating>>(result)
    }

    @Test
    fun `eleven is not valid`() {
        val result = Rating.of(11)
        assertIs<Invalid<Unit>>(result)
    }

    @Test
    fun `minus one is not valid`() {
        val result = Rating.of(-1)
        assertIs<Invalid<Unit>>(result)
    }
}
