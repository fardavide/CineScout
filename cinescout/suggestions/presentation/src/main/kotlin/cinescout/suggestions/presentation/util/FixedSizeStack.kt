package cinescout.suggestions.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class FixedSizeStack<T : Any> private constructor(
    private val size: Int,
    collection: Collection<T>
) {

    init {
        require(size > 0) { "size must be greater than 0" }
    }

    private val collection = collection.take(size).toSet()

    fun all(): Set<T> =
        collection

    fun isEmpty() =
        collection.isEmpty()

    fun isFull() =
        collection.size == size

    fun head(): T? =
        collection.firstOrNull()

    fun join(collection: Collection<T>): FixedSizeStack<T> =
        FixedSizeStack(
            size = size,
            collection = listOf(this.collection.take(1), collection, this.collection.drop(1)).flatten()
        )

    fun <K> joinBy(collection: Collection<T>, selector: (T) -> K): FixedSizeStack<T> {
        val list = listOf(this.collection.take(1), collection, this.collection.drop(1)).flatten()
        return FixedSizeStack(
            size = size,
            collection = list.distinctBy(selector)
        )
    }

    fun join(element: T): FixedSizeStack<T> =
        join(listOf(element))

    fun <K> joinBy(element: T, selector: (T) -> K): FixedSizeStack<T> =
        joinBy(listOf(element), selector)

    fun pop(): Pair<FixedSizeStack<T>, T?> =
        FixedSizeStack(size = size, collection = collection.drop(1)) to collection.firstOrNull()

    operator fun contains(element: T) =
        collection.contains(element)

    companion object {

        fun <T : Any> empty(size: Int): FixedSizeStack<T> =
            FixedSizeStack(size, emptyList())

        fun <T : Any> fromCollection(size: Int, collection: Collection<T>): FixedSizeStack<T> =
            FixedSizeStack(size, collection)
    }
}

internal fun <T : Any> StateFlow<FixedSizeStack<T>>.isEmpty() =
    value.isEmpty()

internal fun <T : Any> StateFlow<FixedSizeStack<T>>.isFull() =
    value.isFull()

internal operator fun <T : Any> StateFlow<FixedSizeStack<T>>.contains(element: T) =
    value.contains(element)

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.join(collection: Collection<T>) {
    value = value.join(collection)
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.join(element: T) {
    value = value.join(element)
}

internal fun <T : Any, K> MutableStateFlow<FixedSizeStack<T>>.joinBy(element: T, selector: (T) -> K) {
    value = value.joinBy(element, selector)
}

internal fun <T : Any, K> MutableStateFlow<FixedSizeStack<T>>.joinBy(collection: Collection<T>, selector: (T) -> K) {
    value = value.joinBy(collection, selector)
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.pop() {
    value = value.pop().first
}
