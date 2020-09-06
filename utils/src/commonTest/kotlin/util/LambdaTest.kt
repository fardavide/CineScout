package util

import assert4k.*
import kotlin.test.*

class LambdaTest {

    @Test
    fun `plus works correctly`() {
        var result = ""
        val first = { result += "hello " }
        val second = { result += "world" }

        val merge = first + second
        merge()

        assert that result equals "hello world"
    }
}
