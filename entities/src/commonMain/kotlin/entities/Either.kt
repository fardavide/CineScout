package entities

import entities.Either.Left
import entities.Either.Right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.jvm.JvmName

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

    fun leftOrThrow(): A =
        leftOrNull() ?: throw NoValueError(rightOrThrow())

    fun rightOrThrow(): B =
        rightOrNull() ?: throw NoValueError(leftOrThrow())

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
     * The given function is applied if this is a [Right].
     *
     * Example:
     * ```
     * Right(12).map { "flower" } // Result: Right("flower")
     * Left(12).map { "flower" }  // Result: Left(12)
     * ```
     */
    inline fun <C> map(f: (B) -> C): Either<A, C> =
        flatMap { Right(f(it)) }

    /**
     * The given function is applied if this is a [Left].
     *
     * Example:
     * ```
     * Right(12).mapLeft { "flower" } // Result: Right(12)
     * Left(12).mapLeft { "flower" }  // Result: Left("flower")
     * ```
     */
    inline fun <C> mapLeft(f: (A) -> C): Either<C, B> =
        flatMapLeft { Left(f(it)) }

    /**
     * Executed [alsoBlock] if [isRight]
     */
    inline fun ifRight(alsoBlock: (B) -> Unit) = also {
        if (isRight()) alsoBlock(rightOrThrow())
    }


    data class Left<out A, out B>(val a: A) : Either<A, B>() {
        override val isLeft = true
        override val isRight = false
    }

    data class Right<out A, out B>(val b: B) : Either<A, B>() {
        override val isLeft = false
        override val isRight = true
    }


    interface FixBlock {

        operator fun <A, B> Either<A, B>.component1(): B =
            rightOrThrow()
    }

    class FlowFixBlock<A, B>(
        collector: FlowCollector<Either<A, B>>
    ): FlowCollector<Either<A, B>> by collector, FixBlock {

        @JvmName("emitLeft")
        suspend fun emit(a: A) {
            emit(Left(a))
        }

        @JvmName("emitRight")
        suspend fun emit(b: B) {
            emit(Right(b))
        }
    }


    class NoValueError(val other: Any?) : Exception()


    companion object : Invokable {

        fun <A> left(left: A): Either<A, Nothing> = Left(left)

        fun <B> right(right: B): Either<Nothing, B> = Right(right)

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
        inline fun <A, B> conditionally(test: Boolean, ifFalse: () -> A, ifTrue: () -> B): Either<A, B> =
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
        inline fun <B> conditionally(test: Boolean, ifTrue: () -> B): Either<Unit, B> =
            if (test) right(ifTrue()) else left(Unit)

        /**
         * Will create an [Either] from the receiver [B], it will be [Either.Right] if [test] returns `true`,
         * else a [Either.Left] with the result of [ifFalse]
         *
         * @param test expression to evaluate and build an [Either]
         * @param ifFalse function to create a [Either.Left] in case of false result of test
         *
         * @return [Either.Right] if evaluation succeed, [Either.Left] otherwise
         */
        inline fun <A, B> B.orLeft(test: (B) -> Boolean, ifFalse: () -> A): Either<A, B> =
            conditionally(test(this), ifFalse, { this })

        /**
         * Evaluate the result of [block] and shortcut if any error happens inside it.
         * @return [Either] of [E] and [B]
         */
        inline fun <A : E, B, E : Error> fix(block: FixBlock.() -> Either<A, B>): Either<E, B> =
            try {
                block(object : FixBlock {})
            } catch (e: NoValueError) {
                @Suppress("UNCHECKED_CAST")
                (e.other as E).left()
            }

        /**
         * Create a [Flow] by evaluating the result of [block] and shortcut if any error happens inside it.
         * @return [Flow] [Either] of [E] and [B]
         */
        inline fun <A : E, B, E : Error> fixFlow(
            crossinline block: suspend FlowFixBlock<E, B>.() -> Unit
        ): Flow<Either<E, B>> =
            flow {
                try {
                    block(FlowFixBlock(this))
                } catch (e: NoValueError) {
                    @Suppress("UNCHECKED_CAST")
                    emit((e.other as E).left())
                }
            }
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
 * Binds the given function across [Either.Left].
 *
 * @param f The function to bind across [Either.Left].
 */
inline fun <A, B, C> Either<A, B>.flatMapLeft(f: (A) -> Either<C, B>): Either<C, B> =
    fold({ f(it) }, { Right(it) })

/**
 * Binds the given function across [Either.Right].
 *
 * @param f The function to bind across [Either.Right].
 */
inline infix fun <A, B, C> Either<A, B>.then(f: (B) -> Either<A, C>): Either<A, C> =
    flatMap(f)

/**
 * Binds the given [Either] across [Either.Right].
 *
 * @param next The Either to bind across [Either.Right].
 */
infix fun <A, B, C> Either<A, B>.then(next: Either<A, C>): Either<A, C> =
    flatMap { next }

/**
 * Binds the given [C] across [Either.Left].
 *
 * @param left The Either to bind across [Either.Left].
 */
infix fun <A, B, C> Either<A, B>.or(left: C): Either<C, B> =
    mapLeft { left }

/**
 * Binds the given function across [Either.Left].
 *
 * @param left The function to bind across [Either.Left].
 */
infix fun <A, B, C> Either<A, B>.or(left: (A) -> C): Either<C, B> =
    mapLeft { left(it) }

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
