package util

/**
 * Merge 2 lambdas
 * @return lambda that once invoked it will invoke the receiver lambda and then [other]
 *
 * Example
 * ```kotlin
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

/**
 * Use this on a `when` block for ensure that is exhaustive
 *
 * Example
 * ```kotlin
when (sealedClass) {
    is One -> {}
    is Two -> {}
}.exhaustive
 * ```
 * Here we will have error if we remove the branch 'Two'
 */
val <T : Any> T.exhaustive get()= this
