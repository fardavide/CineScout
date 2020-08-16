package client

import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Base ViewModel for clients
 * Implements [DispatchersProvider] for avoid to use hard-coded Dispatchers
 */
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
    fun <T> ViewStateFlow<T>.set(state: ViewState<T>) {
        mutable.value = state
    }

    /**
     * Set a [ViewState.Success] with given [T] data into the receiver [ViewStateFlow]
     */
    fun <T> ViewStateFlow<T>.set(data: T) {
        mutable.value = ViewState(data)
    }

    /**
     * Set a [ViewState.Error] with given [throwable] into the receiver [ViewStateFlow]
     */
    fun <T> ViewStateFlow<T>.set(throwable: Throwable) {
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
}

@Suppress("UNCHECKED_CAST")
private val <T> ViewStateFlow<T>.mutable
    get() = (this as MutableStateFlow<ViewState<T>>)
