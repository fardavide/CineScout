package cinescout.network

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class DualSourceCall(
    val isFirstSourceLinked: suspend () -> Boolean,
    val isSecondSourceLinked: suspend () -> Boolean
) {

    suspend inline operator fun invoke(
        crossinline firstSourceCall: () -> Either<NetworkError, Unit>,
        crossinline secondSourceCall: () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> =
        coroutineScope {
            val isFirstSourceLinked = isFirstSourceLinked()
            val isSecondSourceLinked = isSecondSourceLinked()
            if (isFirstSourceLinked.not() && isSecondSourceLinked.not()) {
                return@coroutineScope NetworkError.Unauthorized.left()
            }
            val firstSourceResult = async {
                if (isFirstSourceLinked) firstSourceCall()
                else Unit.right()
            }
            val secondSourceResult = async {
                if (isSecondSourceLinked) secondSourceCall()
                else Unit.right()
            }
            firstSourceResult.await().flatMap { secondSourceResult.await() }
        }
}
