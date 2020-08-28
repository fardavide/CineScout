package client

import assert4k.*
import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.None
import entities.util.ViewStateTest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

@Suppress("ClassName")
class ViewState_Test : ViewStateTest() {

    @Test
    fun `Has None as default initial state`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        assert that flow.value equals None
    }

    @Test
    fun `Has provided parameter as initial state`() = runBlockingTest {
        val flow = ViewStateFlow<Int>(Loading)
        assert that flow.value equals Loading
    }

    @Test
    fun `Has provided parameter as initial data`() = runBlockingTest {
        val flow = ViewStateFlow(15)
        assert that flow.data equals 15
    }

    @Test
    fun `Can publish from CineViewModel`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        assert that flow.data `is` Null

        flow.data = 10
        assert that flow.data equals 10
    }

    @Test
    fun `Can be collected`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        val result = mutableListOf<Int>()
        val collect = launch {
            flow.onlyData().drop(1).take(5).toList(result)
        }
        launch {
            repeat(99) {
                flow.data = it * 3
            }
        }
        joinAll(collect)
        assert that result equals listOf(3, 6, 9, 12, 15)
    }

    @Test
    fun `emitCatching emits Error on exception`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        flow.emitCatching { throw Exception() }
        assert that (flow.first() is Error)
    }

    @Test
    fun `emitCatching can set loading on start`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        val result = mutableListOf<ViewState<Int, Error>>()
        val collect = launch {
            flow.take(2).toList(result)
        }
        flow.emitCatching(initLoading = true) { 0 }
        joinAll(collect)
        assert that result `contains all` setOf(None, Loading)
    }

    @Test
    fun `next works correctly`() = runBlockingTest {
        val throwable = Exception("")

        val flow = ViewStateFlow<Int>()
        assert that flow.value equals None

        var result: ViewState<Int, Error> = flow.value
        launch {
            result = flow.next()
        }
        flow.state = Error(throwable)
        assert that result `is` type<Error>()
    }

    @Test
    fun `nextData works correctly`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        assert that flow.value equals None

        var result = 0
        launch {
            result = flow.nextData()
        }
        flow.data = 5
        assert that result equals 5
    }

    sealed class SealedError : ViewState.Error() {
        object One : SealedError()
        object Two : SealedError()
    }

    @Test
    fun `create with error type`() = runBlockingTest {
        val flow = ViewStateFlow<Int, SealedError>()
        assert that flow.value equals None
    }

    @Test
    fun `can set data with error type`() = runBlockingTest {
        val flow = ViewStateFlow<Int, SealedError>()
        flow.data = 5
        assert that flow.data equals 5
    }

    @Test
    fun `sealed error works correctly`() = runBlockingTest {
        val flow = ViewStateFlow<Int, SealedError>()
        flow set SealedError.One

        // This test should be considered invalid if IDE show the warning that the 'when' expression is no exhaustive
        when (flow.error) {
            SealedError.One -> {}
            SealedError.Two -> throw IllegalStateException("This should not happen")
        }
    }
}
