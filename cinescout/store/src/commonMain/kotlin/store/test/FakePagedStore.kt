package store.test

import arrow.core.Either
import cinescout.error.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import store.PagedData
import store.PagedStore
import store.Paging

class FakePagedStore<T, P : Paging>(
    internal val flow: Flow<Either<DataError, PagedData<T, P>>>,
    internal val onLoadMore: () -> Unit,
    internal val onLoadAll: () -> Unit
) : PagedStore<T, P>, Flow<Either<DataError, PagedData<T, P>>> by flow {

    var didInvokeLoadAll: Boolean = false
        private set

    var loadMoreInvocationCount: Int = 0
        private set

    override suspend fun getAll(): Either<DataError, List<T>> =
        loadAll().filterIntermediatePages().first().map { it.data }

    override fun loadAll(): PagedStore<T, P> {
        didInvokeLoadAll = true
        onLoadAll()
        return this
    }

    override fun loadMore(): PagedStore<T, P> {
        loadMoreInvocationCount++
        onLoadMore()
        return this
    }
}
