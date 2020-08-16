package client.viewModel

import client.DispatchersProvider
import client.ViewState
import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.Success
import client.ViewStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * Base ViewModel for clients
 * Implements [DispatchersProvider] for avoid to use hard-coded Dispatchers
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface CineViewModel : DispatchersProvider {
    val scope: CoroutineScope

    /**
     * This function should close all the pending Channel's
     */
    fun closeChannels() {

    }

    fun onClear() {
        closeChannels()
        scope.cancel()
    }

    /**
     * Set a [ViewState] into the receiver [ViewStateFlow]
     */
    infix fun <T> ViewStateFlow<T>.set(state: ViewState<T>) {
        mutable.value = state
    }

    /**
     * Set a [ViewState.Success] with given [T] data into the receiver [ViewStateFlow]
     */
    infix fun <T> ViewStateFlow<T>.set(data: T) {
        mutable.value = ViewState(data)
    }

    /**
     * Set a [ViewState.Error] with given [throwable] into the receiver [ViewStateFlow]
     */
    infix fun <T> ViewStateFlow<T>.set(throwable: Throwable) {
        mutable.value = ViewState<T>(throwable)
    }

    /**
     * Get or set a [ViewState] into the receiver [ViewStateFlow]
     */
    var <T> ViewStateFlow<T>.state: ViewState<T>
        get() = value
        set(value) {
            mutable.value = value
        }

    /**
     * Get or set a [ViewState.Success] with given [T] data into the receiver [ViewStateFlow]
     */
    var <T> ViewStateFlow<T>.data: T?
        get() = (value as? Success)?.data
        set(value) {
            requireNotNull(value) { "Cannot set a null data" }
            set(value)
        }

    /**
     * Get or set a [ViewState.Error] with given throwable into the receiver [ViewStateFlow]
     */
    var <T> ViewStateFlow<T>.error: Throwable
        get() = (value as? Error)?.throwable ?: throw IllegalStateException("Current state is not an Error")
        set(value) {
            set(value)
        }

    /**
     * Execute safely the given [block]:
     * * if it success, set a [ViewState.Success] with result [T] data into the receiver [ViewStateFlow]
     * * if fails, set a [ViewState.Error] with given throwable into the receiver [ViewStateFlow]
     *
     * @param initLoading when `true`, set [ViewState.Loading] at the beginning of the operation.
     *   default is `false`
     */
    suspend fun <T> ViewStateFlow<T>.emitCatching(initLoading: Boolean = false, block: suspend () -> T) {
        if (initLoading) state = Loading
        state = runCatching { block() }
            .fold(onSuccess = ::Success, onFailure = ::Error)
    }

    /**
     * This terminal operator will broadcast the receiver [Flow] of [T], folding data / Error into given
     * [ViewStateFlow]
     */
    @JvmName("T_Flow_broadcast_in")
    @JsName("T_Flow_broadcast_in")
    suspend fun <T> Flow<T>.broadcastFoldingIn(viewStateFlow: ViewStateFlow<T>) {
        catch { viewStateFlow.error = it }
            .collect { viewStateFlow.data = it }
    }

    /**
     * This terminal operator will broadcast the receiver [Flow] of [ViewState] of [T], folding Success / Error into
     * given [ViewStateFlow]
     */
    @JvmName("ViewState_T_Flow_broadcast_in")
    @JsName("ViewState_T_Flow_broadcast_in")
    suspend fun <T> Flow<ViewState<T>>.broadcastFoldingIn(viewStateFlow: ViewStateFlow<T>) {
        catch { viewStateFlow.error = it }
            .collect { viewStateFlow.state = it }
    }
}

@Suppress("UNCHECKED_CAST")
private val <T> ViewStateFlow<T>.mutable
    get() = (this as MutableStateFlow<ViewState<T>>)
