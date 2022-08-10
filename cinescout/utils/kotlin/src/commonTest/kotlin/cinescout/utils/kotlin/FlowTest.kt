package cinescout.utils.kotlin

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
            assertEquals("hello world how are you", awaitItem())
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
            assertEquals("hello world how are you", awaitItem())
            assertEquals("hi davide where are you", awaitItem())
            assertEquals("hi davide who are you", awaitItem())
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
}
