package cinescout.store

import arrow.core.continuations.either
import arrow.core.right
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

fun <T> combinePagedStores(
    first: PagedStore<T, Paging.Page.SingleSource>,
    second: PagedStore<T, Paging.Page.SingleSource>
): PagedStore<T, Paging.Page.DualSources> =
    PagedStoreImpl(
        flow = combine(first, second) { firstEither, secondEither ->
            either {
                val firstData = firstEither.bind()
                val secondData = secondEither.bind()

                PagedData.Remote(
                    data = firstData.data + secondData.data,
                    paging = Paging.Page.DualSources(
                        first = firstData.paging,
                        second = secondData.paging
                    )
                )
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

fun <T> emptyPagedStore(): PagedStore<T, Paging.Page.SingleSource> =
    pagedStoreOf(emptyList())
fun <T> dualSourcesEmptyPagedStore(): PagedStore<T, Paging.Page.DualSources> =
    dualSourcesPagedStoreOf(emptyList())

fun <T> pagedStoreOf(data: List<T>): PagedStore<T, Paging.Page.SingleSource> =
    pagedStoreOf(data.toPagedData(Paging.Page.SingleSource.Initial))
fun <T> dualSourcesPagedStoreOf(data: List<T>): PagedStore<T, Paging.Page.DualSources> =
    pagedStoreOf(data.toPagedData(Paging.Page.DualSources.Initial))

fun <T, P : Paging> pagedStoreOf(data: PagedData<T, P>): PagedStore<T, P> =
    PagedStoreImpl(flow = flowOf(data.right()), onLoadMore = {}, onLoadAll = {})
