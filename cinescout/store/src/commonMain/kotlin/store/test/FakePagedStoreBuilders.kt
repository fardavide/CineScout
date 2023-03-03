package store.test

import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.flowOf
import store.PagedData
import store.Paging
import store.builder.toRemotePagedData

fun <T> emptyFakePagedStore(): FakePagedStore<T, Paging> = fakePagedStoreOf(emptyList())

fun <T> fakePagedStoreOf(vararg items: T): FakePagedStore<T, Paging> = fakePagedStoreOf(items.toList())

fun <T> fakePagedStoreOf(data: List<T>): FakePagedStore<T, Paging> =
    fakePagedStoreOf(data.toRemotePagedData(Paging.Page.Initial))

fun <T> fakePagedStoreOf(networkError: NetworkError): FakePagedStore<T, Paging> =
    fakePagedStoreOf(DataError.Remote(networkError))

fun <T> fakePagedStoreOf(dataError: DataError): FakePagedStore<T, Paging> =
    FakePagedStore(flow = flowOf(dataError.left()), onLoadMore = {}, onLoadAll = {})

fun <T, P : Paging> fakePagedStoreOf(data: PagedData<T, P>): FakePagedStore<T, P> =
    FakePagedStore(flow = flowOf(data.right()), onLoadMore = {}, onLoadAll = {})
