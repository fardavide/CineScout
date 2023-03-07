package store

import arrow.core.Either
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class Fetcher<T : Any> @PublishedApi internal constructor(
    val flow: Flow<Either<NetworkOperation, T>>
) {

    companion object {

        fun <T : Any> buildForOperation(
            block: suspend FlowCollector<Either<NetworkOperation, T>>.() -> Unit
        ): Fetcher<T> = Fetcher(flow(block))

        fun <T : Any> forData(flow: Flow<T>): Fetcher<T> = Fetcher(flow.map { it.right() })

        inline fun <T : Any> forError(crossinline block: suspend () -> Either<NetworkError, T>): Fetcher<T> =
            Fetcher(flow { emit(block().mapLeft { NetworkOperation.Error(it) }) })

        fun <T : Any> forError(flow: Flow<Either<NetworkError, T>>): Fetcher<T> =
            Fetcher(flow.map { either -> either.mapLeft { NetworkOperation.Error(it) } })

        inline fun <T : Any> forOperation(
            crossinline block: suspend () -> Either<NetworkOperation, T>
        ): Fetcher<T> = Fetcher(flow { emit(block()) })

        fun <T : Any> forOperation(flow: Flow<Either<NetworkOperation, T>>): Fetcher<T> = Fetcher(flow)
    }
}
