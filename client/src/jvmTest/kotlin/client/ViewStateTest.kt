package client

import assert4k.*
import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.None
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test

class ViewStateTest {

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

        flow.publishFromViewModel(10)
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
                flow.publishFromViewModel(it * 3)
            }
        }
        joinAll(collect)
        assert that result equals listOf(3, 6, 9, 12, 15)
    }

    @Test
    fun `emitCatching emits Error on exception`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        flow.emitCatchingFromViewModel { throw Exception() }
        assert that (flow.first() is Error)
    }

    @Test
    fun `emitCatching can set loading on start`() = runBlockingTest {
        val flow = ViewStateFlow<Int>()
        val result = mutableListOf<ViewState<Int>>()
        val collect = launch {
            flow.take(2).toList(result)
        }
        flow.emitCatchingFromViewModel(initLoading = true) { 0 }
        joinAll(collect)
        assert that result `contains all` setOf(None, Loading)
    }

    private fun <T : Any> ViewStateFlow<T>.publishFromViewModel(value: T) {
        val vm = object : CineViewModel, DispatchersProvider by TestDispatchersProvider() {
            override val scope = TestCoroutineScope()
            fun publish() {
                this@publishFromViewModel.set(value)
            }
        }
        vm.publish()
    }

    private suspend fun <T : Any> ViewStateFlow<T>.emitCatchingFromViewModel(
        initLoading: Boolean = false,
        block: suspend () -> T
    ) {
        val vm = object : CineViewModel, DispatchersProvider by TestDispatchersProvider() {
            override val scope = TestCoroutineScope()
            suspend fun publish() {
                emitCatching(initLoading, block)
            }
        }
        vm.publish()
    }
}
