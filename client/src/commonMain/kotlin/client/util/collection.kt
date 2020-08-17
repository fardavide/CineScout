package client.util

/**
 * @return [List] of nullable [T]
 * If receiver [Collection] has more elements of [count] only take part of them, else fill empty spaces with nulls
 */
fun <T> Collection<T>.takeOrFill(count: Int): List<T?> =
    if (size >= count) take(count)
    else this + (size until count).map { null }
