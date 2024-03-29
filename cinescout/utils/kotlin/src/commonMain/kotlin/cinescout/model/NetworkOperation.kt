package cinescout.model

import arrow.core.Either
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import cinescout.error.NetworkError

sealed interface NetworkOperation {

    object Skipped : NetworkOperation
    data class Error(val error: NetworkError) : NetworkOperation
}

fun Either<NetworkOperation, Unit>.handleSkippedAsRight(): Either<NetworkError, Unit> =
    recover { networkOperation ->
        when (networkOperation) {
            NetworkOperation.Skipped -> Unit.right()
            is NetworkOperation.Error -> networkOperation.error.left()
        }.bind()
    }
