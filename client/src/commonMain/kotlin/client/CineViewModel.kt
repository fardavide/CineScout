package client

import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Base ViewModel for clients
 * Implements [DispatchersProvider] for avoid to use hard-coded Dispatchers
 */
interface CineViewModel : DispatchersProvider {
    val scope: CoroutineScope

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewStateFlow<T>.set(state: ViewState<T>) {
        mutable.value = state
    }

    fun <T> ViewStateFlow<T>.set(data: T) {
        mutable.value = ViewState(data)
    }

    var <T> ViewStateFlow<T>.state: ViewState<T>
        get() = value
        set(value) {
            mutable.value = value
        }

    suspend fun <T> ViewStateFlow<T>.emitCatching(initLoading: Boolean = false, block: suspend () -> T) {
        if (initLoading) state = Loading
        state = runCatching { block() }
            .fold(onSuccess = ::Success, onFailure = ::Error)
    }
}

@Suppress("UNCHECKED_CAST")
private val <T> ViewStateFlow<T>.mutable
    get() = (this as MutableStateFlow<ViewState<T>>)
