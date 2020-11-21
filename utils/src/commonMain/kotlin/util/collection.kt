package util

fun <T, C : Collection<T>> C.takeIfNotEmpty() =
    takeIf { it.isNotEmpty() }

inline fun <T, C : Collection<T>> C.useIfNotEmpty(block: (C) -> Unit) =
    takeIfNotEmpty()?.let(block)

inline fun <K, V> MutableMap<K, V>.getOrPutIfNotNull(key: K, block: () -> V?): V? =
    get(key) ?: block()?.also { put(key, it) }
