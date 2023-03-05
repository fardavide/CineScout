package store

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

class Fetcher<T : Any> @PublishedApi internal constructor(
    private val fetch: suspend () -> Either<NetworkOperation, T>
) {

    internal suspend operator fun invoke() = fetch()

    companion object {

        inline fun <T : Any> forError(crossinline block: suspend () -> Either<NetworkError, T>): Fetcher<T> =
            Fetcher { block().mapLeft { NetworkOperation.Error(it) } }

        inline fun <T : Any> forOperation(
            crossinline block: suspend () -> Either<NetworkOperation, T>
        ): Fetcher<T> = Fetcher { block() }
    }
}
