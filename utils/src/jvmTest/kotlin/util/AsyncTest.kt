package util

import assert4k.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.DelayController
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*
import kotlin.time.milliseconds

class AsyncTest {

    @Test
    fun `await works correctly`() = runBlockingTest {
        val list = mutableListOf<Int>()

        launch {
            delay(500)
            list += 1
        }

        val time = measureVirtualTimeMillis {
            await { list.isNotEmpty() }
        }

        assert that time between 480 .. 520
    }

    @Test
    fun `with timeout if condition is met`() = runBlockingTest {
        val list = mutableListOf<Int>()

        launch {
            delay(500)
            list += 1
        }

        val time = measureVirtualTimeMillis {
            await(700.milliseconds) { list.isNotEmpty() }
        }
        currentTime

        assert that time between 480 .. 520
    }

    @Test
    fun `with timeout if condition is not met`() = runBlockingTest {
        val list = mutableListOf<Int>()

        val time = measureVirtualTimeMillis {
            await(200.milliseconds) { list.isNotEmpty() }
        }

        assert that time between 180 .. 220
    }

    @Test
    fun `with timeout if condition is not met with runBlockingTest`() = runBlockingTest {
        val list = emptyList<Int>()
        await(200.milliseconds) { list.isNotEmpty() }
    }
}

private inline fun DelayController.measureVirtualTimeMillis(block: () -> Unit): Long {
    val start = currentTime
    block()
    return currentTime - start
}
