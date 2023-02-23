package store

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation

class PagedFetcher<T : Any> @PublishedApi internal constructor(
    private val fetch: suspend (page: Paging.Page) -> Either<NetworkOperation, PagedData.Remote<T>>
) {

    internal suspend operator fun invoke(page: Paging.Page) = fetch(page)

    companion object {

        inline fun <T : Any> forError(
            crossinline block: suspend (page: Paging.Page) -> Either<NetworkError, PagedData.Remote<T>>
        ): PagedFetcher<T> =
            PagedFetcher { page -> block(page).mapLeft { networkError -> NetworkOperation.Error(networkError) } }

        inline fun <T : Any> forOperation(
            crossinline block: suspend (page: Paging.Page) -> Either<NetworkOperation, PagedData.Remote<T>>
        ): PagedFetcher<T> = PagedFetcher { page -> block(page) }
    }
}
