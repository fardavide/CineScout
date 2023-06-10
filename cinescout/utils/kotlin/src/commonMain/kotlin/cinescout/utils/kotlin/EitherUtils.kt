package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

fun <A, B> Either<A, B>.mapToUnit(): Either<A, Unit> = map { }

operator fun <A, B> Either<A, List<B>>.plus(other: Either<A, List<B>>): Either<A, List<B>> =
    this.flatMap { list1 ->
        other.map { list2 ->
            list1 + list2
        }
    }

fun <A, B> sum(first: Either<A, List<B>>, second: Either<A, List<B>>): Either<A, List<B>> = first + second

fun <B> Either<NetworkOperation, List<B>>.handleSkippedAsEmpty(): Either<NetworkError, List<B>> =
    recover { networkOperation ->
        when (networkOperation) {
            is NetworkOperation.Skipped -> emptyList<B>().right()
            is NetworkOperation.Error -> networkOperation.error.left()
        }.bind()
    }
