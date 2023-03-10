package cinescout.store5

import arrow.core.Either
import arrow.core.getOrElse
import cinescout.error.NetworkError
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
    }
}

class FetchException(val error: NetworkError) : Exception()
