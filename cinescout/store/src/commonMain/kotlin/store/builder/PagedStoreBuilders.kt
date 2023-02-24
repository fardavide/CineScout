package store.builder

import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.flowOf
import store.PagedData
import store.PagedStore
import store.PagedStoreImpl
import store.Paging

fun <T> emptyPagedStore(): PagedStore<T, Paging> = pagedStoreOf(emptyList())

fun <T> pagedStoreOf(vararg items: T): PagedStore<T, Paging> = pagedStoreOf(items.toList())

fun <T> pagedStoreOf(data: List<T>): PagedStore<T, Paging> =
    pagedStoreOf(data.toRemotePagedData(Paging.Page.Initial))

fun <T> pagedStoreOf(networkError: NetworkError): PagedStore<T, Paging> =
    pagedStoreOf(DataError.Remote(networkError))

fun <T> pagedStoreOf(dataError: DataError): PagedStore<T, Paging> =
    PagedStoreImpl(flow = flowOf(dataError.left()), onLoadMore = {}, onLoadAll = {})

fun <T, P : Paging> pagedStoreOf(data: PagedData<T, P>): PagedStore<T, P> =
    PagedStoreImpl(flow = flowOf(data.right()), onLoadMore = {}, onLoadAll = {})
