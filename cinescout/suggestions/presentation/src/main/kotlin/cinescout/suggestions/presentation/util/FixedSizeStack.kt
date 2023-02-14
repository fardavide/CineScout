package cinescout.suggestions.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class FixedSizeStack<T : Any> private constructor(
    val size: Int,
    collection: Collection<T>
) : Stack<T>(collection.take(size)) {

    init {
        require(size > 0) { "size must be greater than 0" }
    }

    fun isFull() = collection.size == size

    companion object {

        fun <T : Any> empty(size: Int): FixedSizeStack<T> = FixedSizeStack(size, emptyList())

        fun <T : Any> fromCollection(size: Int, collection: Collection<T>): FixedSizeStack<T> =
            FixedSizeStack(size, collection)
    }
}

internal fun <T : Any> FixedSizeStack<T>.join(element: T): FixedSizeStack<T> = join(listOf(element))

internal fun <T : Any> FixedSizeStack<T>.join(collection: Collection<T>): FixedSizeStack<T> =
    FixedSizeStack.fromCollection(
        size = size,
        collection = listOf(all().take(1), collection, all().drop(1)).flatten()
    )

internal fun <T : Any, K> FixedSizeStack<T>.joinBy(element: T, selector: (T) -> K): FixedSizeStack<T> =
    joinBy(listOf(element), selector)

internal fun <T : Any, K> FixedSizeStack<T>.joinBy(
    collection: Collection<T>,
    selector: (T) -> K
): FixedSizeStack<T> {
    val list = listOf(all().take(1), collection, all().drop(1)).flatten()
    return FixedSizeStack.fromCollection(
        size = size,
        collection = list.distinctBy(selector)
    )
}

internal fun <T : Any> FixedSizeStack<T>.pop(): Pair<FixedSizeStack<T>, T?> = FixedSizeStack.fromCollection(
    size = size,
    collection = all().drop(1)
) to all().firstOrNull()

internal fun <T : Any> StateFlow<FixedSizeStack<T>>.isEmpty() = value.isEmpty()

internal fun <T : Any> StateFlow<FixedSizeStack<T>>.isFull() = value.isFull()

internal operator fun <T : Any> StateFlow<FixedSizeStack<T>>.contains(element: T) = value.contains(element)

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.join(collection: Collection<T>) {
    value = value.join(collection)
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.join(element: T) {
    value = value.join(element)
}

internal fun <T : Any, K> MutableStateFlow<FixedSizeStack<T>>.joinBy(element: T, selector: (T) -> K) {
    value = value.joinBy(element, selector)
}

internal fun <T : Any, K> MutableStateFlow<FixedSizeStack<T>>.joinBy(
    collection: Collection<T>,
    selector: (T) -> K
) {
    value = value.joinBy(collection, selector)
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.pop() {
    value = value.pop().first
}
