package store

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

class PagedFetcher<T : Any, P : Paging.Page> @PublishedApi internal constructor(
    private val fetch: suspend (page: P) -> Either<NetworkOperation, PagedData.Remote<T, P>>
) {

    internal suspend operator fun invoke(page: P) = fetch(page)

    companion object {

        inline fun <T : Any, P : Paging.Page> forError(
            crossinline block: suspend (page: P) -> Either<NetworkError, PagedData.Remote<T, P>>
        ): PagedFetcher<T, P> =
            PagedFetcher { page -> block(page).mapLeft { networkError -> NetworkOperation.Error(networkError) } }

        inline fun <T : Any, P : Paging.Page> forOperation(
            crossinline block: suspend (page: P) -> Either<NetworkOperation, PagedData.Remote<T, P>>
        ): PagedFetcher<T, P> =
            PagedFetcher { page -> block(page) }
    }
}
