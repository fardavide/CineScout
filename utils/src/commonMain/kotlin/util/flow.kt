package util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

/**
 * Create a [Flow] that will emit the result of [block] with interval defined by [time]
 * @return [Flow] of [V]
 */
inline fun <V> interval(
    time: Duration,
    initialDelay: Duration = Duration.ZERO,
    crossinline block: suspend () -> V
): Flow<V> = flow {
    delay(initialDelay)
    while (true) {
        emit(block())
        delay(time)
    }
}

/**
 * Create a [Flow] that will emit [Unit] with interval defined by [time]
 * @return [Flow] of [Unit]
 */
fun interval(
    time: Duration,
    initialDelay: Duration = Duration.ZERO
): Flow<Unit> = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(time)
    }
}
