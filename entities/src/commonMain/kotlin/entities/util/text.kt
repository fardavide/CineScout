package entities.util

fun CharSequence.takeIfNotBlank() = takeIf { isNotBlank() }
fun String.takeIfNotBlank() = takeIf { isNotBlank() }

infix fun String.equalsNoCase(other: String?) = equals(other, ignoreCase = true)

infix fun String.ellipseAt(limit: Int): String {
    val end = limit - 3
    return if (length <= end) this else "${slice(0..end)}..."
}
