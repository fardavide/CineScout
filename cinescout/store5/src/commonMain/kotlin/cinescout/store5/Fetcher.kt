package cinescout.store5

import arrow.core.Either
import arrow.core.getOrElse
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher

class EitherFetcher<Key : Any, Network : Any> internal constructor(
    fetcher: Fetcher<Key, Network>
) : Fetcher<Key, Network> by fetcher {

    companion object {

        fun <Key : Any, Network : Any> of(
            fetch: suspend (Key) -> Either<NetworkError, Network>
        ): EitherFetcher<Key, Network> {
            val block: suspend (Key) -> Network = { key: Key ->
                fetch(key).getOrElse { throw FetchException(it) }
            }
            return EitherFetcher(Fetcher.of(block))
        }

        fun <Key : Any, Network : Any> ofFlow(
            flowFactory: (Key) -> Flow<Either<NetworkError, Network>>
        ): EitherFetcher<Key, Network> {
            val block: (Key) -> Flow<Network> = { key: Key ->
                flowFactory(key).map { it.getOrElse { networkError -> throw FetchException(networkError) } }
            }
            return EitherFetcher(Fetcher.ofFlow(block))
        }

        fun <Key : Any, Network : Any> build(
            flowFactory: FlowCollector<Either<NetworkError, Network>>.(Key) -> Unit
        ): EitherFetcher<Key, Network> {
            val block: (Key) -> Flow<Network> = { key: Key ->
                flow { flowFactory(key) }.map { it.getOrElse { networkError -> throw FetchException(networkError) } }
            }
            return EitherFetcher(Fetcher.ofFlow(block))
        }

        fun <Key : Any, Network : Any> ofOperation(
            fetch: suspend (Key) -> Either<NetworkOperation, Network>
        ): EitherFetcher<Key, Network> {
            val block: suspend (Key) -> Network = { key: Key ->
                fetch(key).getOrElse { left ->
                    when (left) {
                        is NetworkOperation.Error -> throw FetchException(left.error)
                        NetworkOperation.Skipped -> throw SkippedFetch
                    }
                }
            }
            return EitherFetcher(Fetcher.of(block))
        }

        fun <Key : Any, Network : Any> ofOperationFlow(
            flowFactory: (Key) -> Flow<Either<NetworkOperation, Network>>
        ): EitherFetcher<Key, Network> {
            val block: (Key) -> Flow<Network> = { key: Key ->
                flowFactory(key).map {
                    it.getOrElse { networkOperation ->
                        when (networkOperation) {
                            is NetworkOperation.Error -> throw FetchException(networkOperation.error)
                            NetworkOperation.Skipped -> throw SkippedFetch
                        }
                    }
                }
            }
            return EitherFetcher(Fetcher.ofFlow(block))
        }

        fun <Key : Any, Network : Any> buildForOperation(
            flowFactory: suspend FlowCollector<Either<NetworkOperation, Network>>.(Key) -> Unit
        ): EitherFetcher<Key, Network> {
            val block: (Key) -> Flow<Network> = { key: Key ->
                flow { flowFactory(key) }.map {
                    it.getOrElse { networkOperation ->
                        when (networkOperation) {
                            is NetworkOperation.Error -> throw FetchException(networkOperation.error)
                            NetworkOperation.Skipped -> throw SkippedFetch
                        }
                    }
                }
            }
            return EitherFetcher(Fetcher.ofFlow(block))
        }
    }
}

class FetchException(val error: NetworkError) : Exception()
object SkippedFetch : Exception()
