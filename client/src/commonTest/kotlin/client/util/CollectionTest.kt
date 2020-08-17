package client.util

import assert4k.assert
import assert4k.equals
import assert4k.that
import kotlin.test.Test

class CollectionTest {

    @Test
    fun `takeOrFill works correctly with lists of right size`() {
        val input = listOf(1, 2, 3, 4, 5)
        assert that input.takeOrFill(5) equals listOf(1, 2, 3, 4, 5)
    }

    @Test
    fun `takeOrFill works correctly with smaller lists`() {
        val input = listOf(1, 2, 3)
        assert that input.takeOrFill(5) equals listOf(1, 2, 3, null, null)
    }

    @Test
    fun `takeOrFill works correctly with bigger lists`() {
        val input = listOf(1, 2, 3, 4, 5, 6, 7)
        assert that input.takeOrFill(5) equals listOf(1, 2, 3, 4, 5)
    }
}
