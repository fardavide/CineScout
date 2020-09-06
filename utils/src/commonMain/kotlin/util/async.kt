package util

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

/**
 * Await while [condition] returns `false`
 * Return when [condition] returns `true`
 */
suspend inline fun await(timeout: Duration = Duration.INFINITE, crossinline condition: () -> Boolean) {
    withTimeoutOrNull(timeout) {
        repeat(Int.MAX_VALUE) {
            if (condition()) return@withTimeoutOrNull
            // Collaborative call for get out of while loop
            delay(1)
        }
    }
}

suspend inline fun <T, V> Collection<T>.mapAsync(
    crossinline map: suspend (T) -> V
): List<V> = coroutineScope {
    map {
        async { map(it) }
    }.map { it.await() }
}

suspend inline fun <T, V : Any> Collection<T>.mapNotNullAsync(
    crossinline map: suspend (T) -> V?
): List<V> = coroutineScope {
    map {
        async { map(it) }
    }.mapNotNull { it.await() }
}
