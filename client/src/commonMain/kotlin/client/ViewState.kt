package client

import client.ViewState
import client.ViewState.Error
import client.ViewState.Loading
import client.ViewState.Success
import client.viewModel.CineViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * Sub-type of [StateFlow] of [ViewState]
 * It allows only [CineViewModel] to publish into it
 */
interface ViewStateFlow<T> : StateFlow<ViewState<T>>

/**
 * @return a new instance of [ViewStateFlow]
 * @param state optional initial [ViewState]
 */
fun <T> ViewStateFlow(state: ViewState<T> = ViewState.None): ViewStateFlow<T> =
    ViewStateFlowImpl(MutableStateFlow(state))

/**
 * @return a new instance of [ViewStateFlow]
 * @param data initial [T] data
 */
fun <T> ViewStateFlow(data: T) =
    ViewStateFlow(ViewState(data))


/**
 * @return next published [ViewState] ot [T]
 */
suspend fun <T> ViewStateFlow<T>.next(): ViewState<T> {
    val current = value
    return first { it != current }
}

/**
 * @return last published [T] data or `null` if none has been published
 */
val <T> ViewStateFlow<T>.data: T? get() =
    value.data

/**
 * @return last published state if data ot [T] or wait for the next one
 */
suspend fun <T> ViewStateFlow<T>.awaitData(): T {
    return onlyData().first()
}

/**
 * @return next published data ot [T]
 */
suspend fun <T> ViewStateFlow<T>.nextData(): T {
    val current = data
    return onlyData().first { it != current }
}

/**
 * @return [Flow] that filter only the published [T] data
 */
fun <T> ViewStateFlow<T>.onlyData(): Flow<T> =
    mapNotNull { it.data }


sealed class ViewState<out T> {
    open val data: T? = null

    object None : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    open class Error(val throwable: Throwable? = null) : ViewState<Nothing>()
    data class Success<T>(override val data: T) : ViewState<T>()

    companion object {
        operator fun <T> invoke(data: T) = Success(data)
        operator fun <T> invoke(throwable: Throwable) = Error(throwable)
    }
}


/**
 * Implement this for being able to publish to [ViewStateFlow]
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ViewStatePublisher {


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


private class ViewStateFlowImpl<T>(underlying: MutableStateFlow<ViewState<T>>) :
    ViewStateFlow<T>,
    MutableStateFlow<ViewState<T>> by underlying
