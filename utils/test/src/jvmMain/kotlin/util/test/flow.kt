package util.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

/**
 * @return the second element [T] of the receiver [Flow]
 */
suspend fun <T> Flow<T>.second(): T =
    take(2).toList().last()
