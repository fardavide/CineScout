package cinescout.utils.kotlin

import app.cash.turbine.test
import cinescout.test.kotlin.TestTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class FlowTest {

    @Test
    fun `combineLatest with 3 flows emits correctly`() = runTest {
        combineLatest(
            flowOf("hello", "hi"),
            flowOf("world", "davide"),
            flowOf("!")
        ) { a, b, c ->
            flowOf("$a $b $c")
        }.test {
            assertEquals("hello world !", awaitItem())
            assertEquals("hi davide !", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `combineLatest with 4 flows emits correctly`() = runTest {
        combineLatest(
            flowOf("hello", "hi"),
            flowOf("world", "davide"),
            flowOf("how", "where", "who"),
            flowOf("are you")
        ) { a, b, c, d ->
            flowOf("$a $b $c $d")
        }.test {
            assertEquals("hello world how are you", awaitItem())
            assertEquals("hi davide where are you", awaitItem())
            assertEquals("hi davide who are you", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `combineToList emits correctly`() = runTest {
        listOf(
            flowOf("hello", "hi"),
            flowOf("world", "davide"),
            flowOf("how", "where", "who"),
            flowOf("are you")
        ).combineToList().map { it.joinToString(separator = " ") }.test {
            assertEquals("hi davide where are you", awaitItem())
            assertEquals("hi davide who are you", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `combineToList emits correctly from an empty list`() = runTest {
        emptyList<Flow<Int>>().combineToList().test {
            assertEquals(emptyList(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `combineToList emits correctly from uncompleted flows`() = runTest {
        listOf(
            flow {
                emit("hello")
                emit("hi")
            },
            flow {
                emit("world")
                emit("davide")
            },
            flow {
                emit("how")
                emit("where")
                emit("who")
            },
            flow {
                emit("are you")
            }
        ).combineToList().map { it.joinToString(separator = " ") }.test {
            assertEquals("hi davide where are you", awaitItem())
            assertEquals("hi davide who are you", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `combineToLazyList emits progressively`() = runTest {
        listOf(
            flow {
                delay(300)
                emit("hello")
            },
            flow {
                delay(100)
                emit("beautiful")
            },
            flow {
                delay(200)
                emit("world")
            }
        ).combineToLazyList().map { it.joinToString(separator = " ") }.test {
            assertEquals("beautiful", awaitItem())
            assertEquals("beautiful world", awaitItem())
            assertEquals("hello beautiful world", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `mixed combineLatest and combineToList emits correctly`() = runTest {
        combineLatest(
            flowOf(1, 2),
            flowOf(3, 4)
        ) { a, b ->
            listOf(
                flowOf(a, a * 2, a * 4),
                flowOf(b, b * 2, b * 4)
            ).combineToList()
        }.test {
            assertEquals(listOf(2, 4), awaitItem())
            assertEquals(listOf(4, 8), awaitItem())
            assertEquals(listOf(8, 16), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `mixed combineLatest and combineToList emits correctly 2`() = runTest {
        combineLatest(
            listOf(1, 2).map { flowOf(it, it * 2) }.combineToList(),
            listOf(3, 4).map { flowOf(it, it * 2) }.combineToList(),
            listOf<Int>().map { flowOf(it, it * 2) }.combineToList(),
            listOf(5, 6).map { flowOf(it, it * 2) }.combineToList()
        ) { a, b, c, d ->
            val flattenList = listOf(a, b, c, d).flatten()
            flowOf(flattenList, flattenList.map { it * 2 })
        }.test {
            assertEquals(listOf(1, 2, 3, 4, 5, 6), awaitItem())
            assertEquals(listOf(2, 4, 6, 8, 10, 12), awaitItem())
            assertEquals(listOf(2, 4, 6, 8, 10, 12), awaitItem())
            assertEquals(listOf(4, 8, 12, 16, 20, 24), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `first not null returns if a value is not null`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = 3
        val flow = flowOf(null, null, expected)

        // when
        val result = flow.firstNotNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `first not null awaits for a non null value`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = 3
        val flow = flow {
            emit(null)
            delay(5.seconds)
            emit(expected)
        }

        // when
        val result = flow.firstNotNull()

        // then
        assertEquals(expected, result)
    }
}
