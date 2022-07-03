package shuttle.utils.kotlin

fun String.takeIfNotBlank(): String? = takeIf { it.isNotBlank() }
