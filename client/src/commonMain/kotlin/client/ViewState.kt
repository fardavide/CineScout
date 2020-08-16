package client

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

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


private class ViewStateFlowImpl<T>(underlying: MutableStateFlow<ViewState<T>>) :
    ViewStateFlow<T>,
    MutableStateFlow<ViewState<T>> by underlying
