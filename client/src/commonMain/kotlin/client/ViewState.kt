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
interface ViewStateFlow<D, E : Error> : StateFlow<ViewState<D, E>> {
    companion object {

        /**
         * @return a new instance of [ViewStateFlow]
         * @param state optional initial [ViewState]
         */
        @Suppress("RemoveExplicitTypeArguments")
        operator fun <D, E : Error> invoke(state: ViewState<D, E> = ViewState.None): ViewStateFlow<D, E> =
            ViewStateFlowImpl<D, E>(MutableStateFlow(state))

        /**
         * @return a new instance of [ViewStateFlow]
         * @param data initial [D] data
         */
        operator fun <D, E : Error> invoke(data: D) =
            ViewStateFlow<D, E>(ViewState(data))
    }
}

/**
 * @return a new instance of [ViewStateFlow]
 * @param state optional initial [ViewState]
 */
fun <D> ViewStateFlow(state: ViewState<D, Error> = ViewState.None): ViewStateFlow<D, Error> =
    ViewStateFlowImpl(MutableStateFlow(state))

/**
 * @return a new instance of [ViewStateFlow]
 * @param data initial [D] data
 */
fun <D> ViewStateFlow(data: D) =
    ViewStateFlow(ViewState(data))


/**
 * @return next published [ViewState] ot [D]
 */
suspend fun <D, E : Error> ViewStateFlow<D, E>.next(): ViewState<D, E> {
    val current = value
    return first { it != current }
}

/**
 * @return last published [D] data or `null` if none has been published
 */
val <D, E : Error> ViewStateFlow<D, E>.data: D? get() =
    value.data

/**
 * @return last published state if data ot [D] or wait for the next one
 */
suspend fun <D, E : Error> ViewStateFlow<D, E>.awaitData(): D {
    return onlyData().first()
}

/**
 * @return next published data ot [D]
 */
suspend fun <D, E : Error> ViewStateFlow<D, E>.nextData(): D {
    val current = data
    return onlyData().first { it != current }
}

/**
 * @return [Flow] that filter only the published [D] data
 */
fun <D, E : Error> ViewStateFlow<D, E>.onlyData(): Flow<D> =
    mapNotNull { it.data }


sealed class ViewState<out D, out E : Error> {
    open val data: D? = null
    @Suppress("LeakingThis", "UNCHECKED_CAST")
    val error: E get() = this as E

    object None : ViewState<Nothing, Nothing>()
    object Loading : ViewState<Nothing, Nothing>()
    open class Error(open val throwable: Throwable? = null) : ViewState<Nothing, Error>()
    data class Success<D>(override val data: D) : ViewState<D, Nothing>()

    companion object {
        operator fun <D> invoke(data: D) = Success(data)
        operator fun invoke(throwable: Throwable) = Error(throwable)
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
    infix fun <D, E : Error> ViewStateFlow<D, E>.set(state: ViewState<D, E>) {
        mutable.value = state
    }

    /**
     * Set a [ViewState.Success] with given [D] data into the receiver [ViewStateFlow]
     */
    infix fun <D, E : Error> ViewStateFlow<D, E>.set(data: D) {
        mutable.value = ViewState(data)
    }

    /**
     * Set a [ViewState.Error] with given [throwable] into the receiver [ViewStateFlow]
     */
    infix fun <D> ViewStateFlow<D, Error>.set(throwable: Throwable) {
        mutable.value = ViewState(throwable)
    }


    /**
     * Set a [ViewState] into the receiver [ViewStateFlow]
     */
    infix fun <D, E : Error> ViewStateFlow<D, E>.set(error: E) {
        @Suppress("UNCHECKED_CAST")
        mutable.value = error as ViewState<D, E>
    }

    /**
     * Get or set a [ViewState] into the receiver [ViewStateFlow]
     */
    var <D, E: Error> ViewStateFlow<D, E>.state: ViewState<D, E>
        get() = value
        set(e) {
            mutable.value = e
        }

    /**
     * Get or set a [ViewState.Success] with given [D] data into the receiver [ViewStateFlow]
     */
    var <D, E : Error> ViewStateFlow<D, E>.data: D?
        get() = (value as? Success)?.data
        set(value) {
            requireNotNull(value) { "Cannot set a null data" }
            set(value)
        }

    /**
     * Get or set a [ViewState.Error] with given throwable into the receiver [ViewStateFlow]
     */
    var <D> ViewStateFlow<D, Error>.error: Throwable?
        get() = (value as? Error)?.throwable
        set(value) {
            requireNotNull(value) { "Cannot set a null error" }
            set(value)
        }

    /**
     * Get or set a [ViewState.Error] with given throwable into the receiver [ViewStateFlow]
     */
    @Suppress("UNCHECKED_CAST")
    var <D, E : Error> ViewStateFlow<D, E>.error: E?
        get() = (value as? E)
        set(e) {
            requireNotNull(e) { "Cannot set a null error" }
            set(e)
        }

    /**
     * Execute safely the given [block]:
     * * if it success, set a [ViewState.Success] with result [D] data into the receiver [ViewStateFlow]
     * * if fails, set a [ViewState.Error] with given throwable into the receiver [ViewStateFlow]
     *
     * @param initLoading when `true`, set [ViewState.Loading] at the beginning of the operation.
     *   default is `false`
     */
    suspend fun <D> ViewStateFlow<D, Error>.emitCatching(initLoading: Boolean = false, block: suspend () -> D) {
        if (initLoading) state = Loading
        state = runCatching { block() }
            .fold(onSuccess = ::Success, onFailure = ::Error)
    }

    /**
     * This terminal operator will broadcast the receiver [Flow] of [D], folding data / Error into given
     * [ViewStateFlow]
     */
    @JvmName("T_Flow_broadcast_in")
    @JsName("T_Flow_broadcast_in")
    suspend fun <D, E : Error> Flow<D>.broadcastFoldingIn(viewStateFlow: ViewStateFlow<D, Error>) {
        catch { viewStateFlow.error = it }
            .collect { viewStateFlow.data = it }
    }

    /**
     * This terminal operator will broadcast the receiver [Flow] of [ViewState] of [D], folding Success / Error into
     * given [ViewStateFlow]
     */
    @JvmName("ViewState_T_Flow_broadcast_in")
    @JsName("ViewState_T_Flow_broadcast_in")
    suspend fun <D> Flow<ViewState<D, Error>>.broadcastFoldingIn(viewStateFlow: ViewStateFlow<D, Error>) {
        catch { viewStateFlow set it }
            .collect { viewStateFlow.state = it }
    }
}

@Suppress("UNCHECKED_CAST")
private val <D, E : Error> ViewStateFlow<D, E>.mutable
    get() = (this as MutableStateFlow<ViewState<D, E>>)


private class ViewStateFlowImpl<D, E : Error>(underlying: MutableStateFlow<ViewState<D, E>>) :
    ViewStateFlow<D, E>,
    MutableStateFlow<ViewState<D, E>> by underlying
