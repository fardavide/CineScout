package entities.util

import assert4k.*
import kotlin.test.*

class PercentageTest {

    @Test
    fun `fails with number negative number`() {
        assert that fails { (-5).percent }
    }

    @Test
    fun `fails with number greater than 100`() {
        assert that fails { 110.percent }
    }

    @Test
    fun `assert that does not fail with 0`() {
        0.percent
    }

    @Test
    fun `assert that does not fail with 100`() {
        100.percent
    }

    @Test
    fun `negative works correctly`() {
        val p = 10.percent
        assert that -p equals 90.percent
    }

    @Test
    fun `Float times works correctly`() {
        val r = 1f * 10.percent
        assert that r equals 0.1f
    }
}
