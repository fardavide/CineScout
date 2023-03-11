package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.flatMap

fun <A, B> Either<A, B>.mapToUnit(): Either<A, Unit> = map { }

operator fun <A, B> Either<A, List<B>>.plus(other: Either<A, List<B>>): Either<A, List<B>> =
    this.flatMap { list1 ->
        other.map { list2 ->
            list1 + list2
        }
    }

fun <A, B> sum(first: Either<A, List<B>>, second: Either<A, List<B>>): Either<A, List<B>> = first + second
