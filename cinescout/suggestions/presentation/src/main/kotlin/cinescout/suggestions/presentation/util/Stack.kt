package cinescout.suggestions.presentation.util

internal open class Stack<T : Any> protected constructor(
    protected val collection: Collection<T>
) {

    fun all(): Set<T> =
        collection.toSet()

    fun isEmpty() =
        collection.isEmpty()

    fun isNotEmpty() =
        collection.isNotEmpty()

    fun head(): T? =
        collection.firstOrNull()

    operator fun contains(element: T) =
        collection.contains(element)

    override fun equals(other: Any?): Boolean =
        other is Stack<*> && collection.toList() == other.collection.toList()

    override fun hashCode() =
        collection.toList().hashCode()

    override fun toString() =
        collection.toString()

    companion object {

        fun <T : Any> empty(): Stack<T> =
            Stack(emptyList())

        fun <T : Any> fromCollection(collection: Collection<T>): Stack<T> =
            Stack(collection)

        fun <T : Any> of(vararg elements: T): Stack<T> =
            Stack(elements.toSet())
    }
}

internal fun <T : Any> Stack<T>.join(element: T): Stack<T> =
    join(listOf(element))

internal fun <T : Any> Stack<T>.join(collection: Collection<T>): Stack<T> =
    Stack.fromCollection(
        collection = listOf(all().take(1), collection, all().drop(1)).flatten()
    )

internal fun <T : Any, K> Stack<T>.joinBy(element: T, selector: (T) -> K): Stack<T> =
    joinBy(listOf(element), selector)

internal fun <T : Any, K> Stack<T>.joinBy(collection: Collection<T>, selector: (T) -> K): Stack<T> {
    val list = listOf(all().take(1), collection, all().drop(1)).flatten()
    return Stack.fromCollection(
        collection = list.distinctBy(selector)
    )
}

internal fun <T : Any> Stack<T>.pop(): Pair<Stack<T>, T?> =
    Stack.fromCollection(collection = all().drop(1)) to all().firstOrNull()
