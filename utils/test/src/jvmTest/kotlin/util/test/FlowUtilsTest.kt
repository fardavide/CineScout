package util.test

import assert4k.*
import kotlinx.coroutines.flow.flow
import kotlin.test.*

internal class FlowUtilsTest : CoroutinesTest {

    @Test
    fun `second works correctly`() = coroutinesTest {

        // given
        val flow = flow {
            var i = 0
            while(true) emit(i++)
        }

        // when
        val result = flow.second()

        // then
        assert that result equals 1
    }
}
