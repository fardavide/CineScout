package entities.util

fun Boolean.takeIfTrue(): Boolean? = takeIf { it }

inline fun <V> Boolean.useIfTrue(block: (Boolean) -> V) = takeIfTrue()?.let(block)
