package entities.util

/**
 * Merge 2 lambdas
 * @return lambda that once invoked it will invoke the receiver lambda and then [other]
 *
 * Example
 * ```
val fist = { print("hello " }
val second = { print("world")

val result = first + second
result() // prints "hello world"
 * ```
 */
inline operator fun (() -> Unit).plus(crossinline other: () -> Unit): () -> Unit = {
    this()
    other()
}
