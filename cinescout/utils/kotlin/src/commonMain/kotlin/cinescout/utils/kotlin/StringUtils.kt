package cinescout.utils.kotlin

import kotlin.reflect.KClass

val <T : Any> KClass<out T>.shortName get() = qualifiedName?.dropWhile {
    it.isUpperCase().not()
} ?: "no-class-name"

fun String.takeIfNotBlank() = takeIf { it.isNotBlank() }
