package entities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onCompletion

/**
 * Merge receiver [Flow] with [other] after the first one is completed
 * E.G.
 * { 1, 2, 3 }
 *   +
 * { 4, 5 }
 *   =
 * { 1, 2, 3, 4, 5 }
 *
 * @param other the [Flow] that will be emitted after the receiver is completed
 */
operator fun <T> Flow<T>.plus(other: Flow<T>) =
    plus { other }

/**
 * Merge receiver [Flow] with [other] after the first one is completed
 * E.G.
 * { 1, 2, 3 }
 *   +
 * { 4, 5 }
 *   =
 * { 1, 2, 3, 4, 5 }
 *
 * @param other a lambada that returns the [Flow] that will be emitted after the receiver is completed.
 *   Differently from the variant that receives a [Flow] directly, this enables us to launch lazily the functions that
 *   returns the [Flow].
 *   In a real case scenario, we can use `login() + { sync() }` where `sync` will be executed only after `login` is
 *   completed.
 */
operator fun <T> Flow<T>.plus(other: () -> Flow<T>): Flow<T> {
    return onCompletion {
        emitAll(other())
    }
}
