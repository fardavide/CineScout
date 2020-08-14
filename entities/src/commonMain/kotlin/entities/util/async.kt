package entities.util

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
