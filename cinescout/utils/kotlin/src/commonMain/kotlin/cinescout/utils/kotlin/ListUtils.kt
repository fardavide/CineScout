package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.None
import arrow.core.Option
import arrow.core.right
import arrow.core.toNonEmptyListOrNone
import arrow.core.toNonEmptyListOrNull

/**
 * @return the first [R] if any, `null` otherwise
 */
inline fun <reified R : Any> List<*>.findInstance(): R? = find { it is R } as? R

/**
 * @return [NonEmptyList] if the list is not empty, otherwise the result of [ifEmpty]
 */
fun <T, A> List<T>.nonEmpty(ifEmpty: () -> A): Either<A, NonEmptyList<T>> =
    toNonEmptyListOrNone().toEither(ifEmpty)

/**
 * @return [NonEmptyList] if the list is not empty, otherwise throws [IndexOutOfBoundsException]
 * @throws IndexOutOfBoundsException if the list is empty
 */
fun <T> List<T>.nonEmptyUnsafe(): NonEmptyList<T> =
    toNonEmptyListOrNull() ?: throw IndexOutOfBoundsException("The list is empty.")

/**
 * @return random element of the list if not empty, otherwise [None]
 */
fun <T : Any> List<T>.randomOrNone(): Option<T> = Option.fromNullable(takeIf { isNotEmpty() }?.random())

/**
 * @return `true` if any element of the list is [Either.Right], otherwise `false`
 */
fun <A, B> List<Either<A, B>>.anyRight(): Boolean = any { either -> either.isRight() }

/**
 * @return list taking only [Either.Right] values
 */
fun <A, B> List<Either<A, B>>.filterRight(): List<B> =
    filterIsInstance<Either.Right<B>>().map { right -> right.value }

/**
 * @return a list with only [Either.Right.value] if any, otherwise the first [Either.Left]
 */
fun <A, B> List<Either<A, B>>.shiftWithAnyRight(): Either<A, List<B>> = when {
    isEmpty() -> emptyList<B>().right()
    anyRight() -> filterRight().right()
    else -> filterIsInstance<Either.Left<A>>().first()
}
