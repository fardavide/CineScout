package testingChannels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

class ExampleOne {

    private class SUT(private val scope: CoroutineScope) {
        val result = MutableStateFlow(0)
        val buffer = Channel<Int>(5)

        init {
            next()

            scope.launch() {
                var i = 0
                while(isActive) {
                    try {
                        buffer.send(i++)
                    } catch (t: Throwable) {
                        break
                    }
                    delay(10)
                }
            }
        }

        fun next() {
            scope.launch {
                runCatching {
                    result.value = buffer.receive()
                }
            }
        }
    }

    // * * * TESTS * * *

    @Test
    fun `one`() = runBlockingTest {
        val sut = SUT(this)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        cleanupTestCoroutines()
    }

    @Test
    fun `two`() = runBlockingTest {
        val sut = SUT(this)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        sut.buffer.close()
        cleanupTestCoroutines()
    }

    @Test
    fun `three`() = runBlockingTest {
        val scope = TestCoroutineScope()
        val sut = SUT(scope)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        sut.buffer.close()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `four`() = runBlockingTest {
        val scope = TestCoroutineScope()
        val sut = SUT(scope)

        sut.result.take(3).collect {
            println(it)
            sut.next()
        }

        scope.cancel()
    }

}
