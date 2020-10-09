package entities

import entities.Either.Left
import entities.Either.Right

/**
 * Thanks to [Arrow-kt](https://arrow-kt.io/)
 */
sealed class Either<out A, out B> {

    fun isLeft(): Boolean = isLeft
    fun isRight(): Boolean = isRight

    internal abstract val isLeft: Boolean
    internal abstract val isRight: Boolean

    fun leftOrNull(): A? =
        if (this is Left) a else null

    fun rightOrNull(): B? =
        if (this is Right) b else null

    /**
     * Applies `ifLeft` if this is a [Left] or `ifRight` if this is a [Right].
     *
     * Example:
     * ```
     * val result: Either<Exception, Value> = possiblyFailingOperation()
     * result.fold(
     *      { log("operation failed with $it") },
     *      { log("operation succeeded with $it") }
     * )
     * ```
     *
     * @param ifLeft the function to apply if this is a [Left]
     * @param ifRight the function to apply if this is a [Right]
     * @return the results of applying the function
     */
    inline fun <C> fold(ifLeft: (A) -> C, ifRight: (B) -> C): C = when (this) {
        is Right -> ifRight(b)
        is Left -> ifLeft(a)
    }

    /**
     * Returns the right value if it exists, otherwise null
     *
     * Example:
     * ```kotlin:ank:playground
     * import arrow.core.Right
     * import arrow.core.Left
     *
     * //sampleStart
     * val right = Right(12).orNull() // Result: 12
     * val left = Left(12).orNull()   // Result: null
     * //sampleEnd
     * fun main() {
     *   println("right = $right")
     *   println("left = $left")
     * }
     * ```
     */
    fun orNull(): B? = fold({ null }, { it })

    /**
     * The given function is applied if this is a `Right`.
     *
     * Example:
     * ```
     * Right(12).map { "flower" } // Result: Right("flower")
     * Left(12).map { "flower" }  // Result: Left(12)
     * ```
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <C> map(f: (B) -> C): Either<A, C> =
        flatMap { Right(f(it)) }


    class Left<out A, out B>(val a: A) : Either<A, B>() {
        override val isLeft = true
        override val isRight = false
    }

    class Right<out A, out B>(val b: B) : Either<A, B>() {
        override val isLeft = false
        override val isRight = true
    }


    companion object : Invokable {

        fun <L> left(left: L): Either<L, Nothing> = Left(left)

        fun <R> right(right: R): Either<Nothing, R> = Right(right)

        fun <A> fromNullable(a: A?): Either<Unit, A> = a?.right() ?: Unit.left()

        /**
         * Will create an [Either] from the result of evaluating the first parameter using the functions
         * provided on second and third parameters. Second parameter represents function for creating
         * an [Either.Left] in case of a false result of evaluation and third parameter will be used
         * to create a [Either.Right] in case of a true result.
         *
         * @param test expression to evaluate and build an [Either]
         * @param ifFalse function to create a [Either.Left] in case of false result of test
         * @param ifTrue function to create a [Either.Right] in case of true result of test
         *
         * @return [Either.Right] if evaluation succeed, [Either.Left] otherwise
         */
        inline fun <L, R> conditionally(test: Boolean, ifFalse: () -> L, ifTrue: () -> R): Either<L, R> =
            if (test) right(ifTrue()) else left(ifFalse())

        /**
         * Will create an [Either] from the result of evaluating the first parameter using the functions
         * provided on second and third parameters. Second parameter represents function for creating
         * an [Either.Left] in case of a false result of evaluation and third parameter will be used
         * to create a [Either.Right] in case of a true result.
         *
         * @param test expression to evaluate and build an [Either]
         * @param ifTrue function to create a [Either.Right] in case of true result of test
         *
         * @return [Either.Right] if evaluation succeed, [Either.Left] otherwise
         */
        inline fun <R> conditionally(test: Boolean, ifTrue: () -> R): Either<Unit, R> =
            if (test) right(ifTrue()) else left(Unit)

        /**
         * Will create an [Either] from the receiver [R], it will be [Either.Right] if [test] returns `true`,
         * else a [Either.Left] with the result of [ifFalse]
         *
         * @param test expression to evaluate and build an [Either]
         * @param ifFalse function to create a [Either.Left] in case of false result of test
         *
         * @return [Either.Right] if evaluation succeed, [Either.Left] otherwise
         */
        inline fun <L, R> R.orLeft(test: (R) -> Boolean, ifFalse: () -> L): Either<L, R> =
            conditionally(test(this), ifFalse, { this })
    }
}

fun <A> A.left(): Either<A, Nothing> = Left(this)
fun <B> B.right(): Either<Nothing, B> = Right(this)
fun <A> Left(a: A): Either<A, Nothing> = Left(a)
fun <B> Right(b: B): Either<Nothing, B> = Right(b)


/**
 * Binds the given function across [Either.Right].
 *
 * @param f The function to bind across [Either.Right].
 */
inline fun <A, B, C> Either<A, B>.flatMap(f: (B) -> Either<A, C>): Either<A, C> =
    fold({ Left(it) }, { f(it) })

/**
 * Binds the given function across [Either.Right].
 *
 * @param f The function to bind across [Either.Right].
 */
inline infix fun <A, B, C> Either<A, B>.then(f: (B) -> Either<A, C>): Either<A, C> =
    flatMap(f)

/**
 * Returns the value from this [Either.Right] or the given argument if this is a [Either.Left].
 *
 * Example:
 * ```
 * Right(12).getOrElse(17) // Result: 12
 * Left(12).getOrElse(17)  // Result: 17
 * ```
 */
inline fun <B> Either<*, B>.getOrElse(default: () -> B): B =
    fold({ default() }, { it })
