package store.ext

import arrow.core.Either
import cinescout.error.DataError
import kotlinx.coroutines.flow.map
import store.PagedData
import store.PagedStore
import store.PagedStoreImpl
import store.Paging

fun <T, R, P : Paging> PagedStore<T, P>.map(
    transform: (Either<DataError, PagedData<T, P>>) ->
    Either<DataError.Remote, PagedData<R, P>>
): PagedStore<R, P> = with(this as PagedStoreImpl<T, P>) {
    PagedStoreImpl(flow.map(transform), onLoadMore, onLoadAll)
}
