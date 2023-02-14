package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.Option
import arrow.core.right
import arrow.core.toNonEmptyListOrNone
import arrow.core.toNonEmptyListOrNull

@Deprecated(
    "Use toNonEmptyListOrNone) instead",
    ReplaceWith("toNonEmptyListOrNone()", "arrow.core.toNonEmptyListOrNone")
)
fun <T> List<T>.nonEmpty(): Option<NonEmptyList<T>> =
    toNonEmptyListOrNone()

fun <T, A> List<T>.nonEmpty(ifEmpty: () -> A): Either<A, NonEmptyList<T>> =
    toNonEmptyListOrNone().toEither(ifEmpty)

fun <T> List<T>.nonEmptyUnsafe(): NonEmptyList<T> =
    toNonEmptyListOrNull() ?: throw IndexOutOfBoundsException("The list is empty.")

fun <T : Any> List<T>.randomOrNone(): Option<T> =
    Option.fromNullable(takeIf { isNotEmpty() }?.random())

fun <A, B> List<Either<A, B>>.anyRight(): Boolean =
    any { either -> either.isRight() }

fun <A, B> List<Either<A, B>>.filterRight(): List<B> =
    filterIsInstance<Either.Right<B>>().map { right -> right.value }

fun <A, B> List<Either<A, B>>.shiftWithAnyRight(): Either<A, List<B>> =
    if (isEmpty()) {
        emptyList<B>().right()
    } else {
        filterIsInstance<Either.Left<A>>().firstOrNull()
            ?: filterRight().right()
    }
