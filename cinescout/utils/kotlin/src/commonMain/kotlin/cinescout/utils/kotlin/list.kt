package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.Option

fun <T> List<T>.nonEmpty(): Option<NonEmptyList<T>> =
    NonEmptyList.fromList(this)

fun <T, A> List<T>.nonEmpty(ifEmpty: () -> A): Either<A, NonEmptyList<T>> =
    NonEmptyList.fromList(this)
        .toEither(ifEmpty)
