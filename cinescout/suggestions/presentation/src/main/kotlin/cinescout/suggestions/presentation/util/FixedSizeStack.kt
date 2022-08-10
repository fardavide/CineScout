package cinescout.suggestions.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow

internal class FixedSizeStack<T : Any> private constructor(
    val size: Int,
    collection: Collection<T>
) {

    init {
        require(size > 0) { "size must be greater than 0" }
    }

    private val collection = collection.take(size)

    fun all(): List<T> =
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

    fun join(element: T): FixedSizeStack<T> =
        join(listOf(element))

    fun pop(): Pair<FixedSizeStack<T>, T?> =
        FixedSizeStack(size = size, collection = collection.drop(1)) to collection.firstOrNull()

    companion object {

        fun <T : Any> empty(size: Int): FixedSizeStack<T> =
            FixedSizeStack(size, emptyList())

        fun <T : Any> fromCollection(size: Int, collection: Collection<T>): FixedSizeStack<T> =
            FixedSizeStack(size, collection)
    }
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.pop() {
    value = value.pop().first
}

internal fun <T : Any> MutableStateFlow<FixedSizeStack<T>>.join(collection: Collection<T>) {
    value = value.join(collection)
}
