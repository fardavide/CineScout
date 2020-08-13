package entities.util

fun <T, C : Collection<T>> C.takeIfNotEmpty() =
    takeIf { it.isNotEmpty() }

inline fun <T, C : Collection<T>> C.useIfNotEmpty(block: (C) -> Unit) =
    takeIfNotEmpty()?.let(block)
