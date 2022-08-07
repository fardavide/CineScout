package cinescout.suggestions.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow

internal data class ThreeSlotsStack<T : Any>(
    val first: T?,
    val second: T?,
    val third: T?
) {

    fun isFull() =

        first != null && second != null && third != null

    fun join(collection: Collection<T>): ThreeSlotsStack<T> {
        val newCollection =
            if (first != null) collection.toList()
            else collection.drop(1)
        return ThreeSlotsStack(
            first = first ?: collection.firstOrNull(),
            second = newCollection.getOrNull(0),
            third = newCollection.getOrNull(1)
        )
    }

    fun join(element: T): ThreeSlotsStack<T> {
        return ThreeSlotsStack(
            first = first ?: element,
            second = if (first == null) null else element,
            third = second
        )
    }

    fun pop(): Pair<ThreeSlotsStack<T>, T?> =
        ThreeSlotsStack(first = second, second = third, third = null) to first

    companion object {

        fun <T : Any> empty(): ThreeSlotsStack<T> =
            ThreeSlotsStack(first = null, second = null, third = null)

        fun <T : Any> from(collection: Collection<T>): ThreeSlotsStack<T> =
            ThreeSlotsStack(
                first = collection.firstOrNull(),
                second = collection.elementAtOrNull(1),
                third = collection.elementAtOrNull(2)
            )
    }
}

internal fun <T : Any> MutableStateFlow<ThreeSlotsStack<T>>.pop() {
    value = value.pop().first
}

internal fun <T : Any> MutableStateFlow<ThreeSlotsStack<T>>.join(collection: Collection<T>) {
    value = value.join(collection)
}
