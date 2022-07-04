package cinescout.utils.kotlin

fun String.takeIfNotBlank(): String? = takeIf { it.isNotBlank() }
