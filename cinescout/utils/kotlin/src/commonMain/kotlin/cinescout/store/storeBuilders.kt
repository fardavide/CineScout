package cinescout.store

import arrow.core.continuations.either
import arrow.core.right
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

fun <T> combinePagedStores(first: PagedStore<T>, second: PagedStore<T>): PagedStore<T> =
    PagedStoreImpl(
        flow = combine(first, second) { firstEither, secondEither ->
            either {
                val firstData = firstEither.bind()
                val secondData = secondEither.bind()

                firstData + secondData
            }
        },
        onLoadAll = {
            first.loadAll()
            second.loadAll()
        },
        onLoadMore = {
            first.loadMore()
            second.loadMore()
        }
    )

fun <T> emptyPagedStore(): PagedStore<T> =
    pagedStoreOf(emptyList<T>())

fun <T> pagedStoreOf(data: List<T>): PagedStore<T> =
    pagedStoreOf(data.toPagedData(Paging.Page(1, 1)))

fun <T> pagedStoreOf(data: PagedData<T>): PagedStore<T> =
    PagedStoreImpl(flow = flowOf(data.right()), onLoadMore = {}, onLoadAll = {})
