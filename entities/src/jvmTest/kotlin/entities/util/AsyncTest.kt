package entities.util

import assert4k.assert
import assert4k.between
import assert4k.that
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.time.milliseconds

class AsyncTest {

    @Test
    fun `await works correctly`() = runBlocking {
        val list = mutableListOf<Int>()

        launch {
            delay(500)
            list += 1
        }

        val time = measureTimeMillis {
            await { list.isNotEmpty() }
        }

        assert that time between 480 .. 520
    }

    @Test
    fun `with timeout if condition is met`() = runBlocking {
        val list = mutableListOf<Int>()

        launch {
            delay(500)
            list += 1
        }

        val time = measureTimeMillis {
            await(700.milliseconds) { list.isNotEmpty() }
        }

        assert that time between 480 .. 520
    }

    @Test
    fun `with timeout if condition is not met`() = runBlocking {
        val list = mutableListOf<Int>()

        val time = measureTimeMillis {
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
