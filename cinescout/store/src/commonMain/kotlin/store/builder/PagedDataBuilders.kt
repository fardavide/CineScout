package store.builder

import store.PagedData
import store.Paging

inline fun <T : Any> mergePagedData(
    first: PagedData.Remote<T, Paging.Page.SingleSource>,
    second: PagedData.Remote<T, Paging.Page.SingleSource>,
    id: (T) -> Any = { it },
    onConflict: (first: T, second: T) -> T
): PagedData.Remote<T, Paging.Page.DualSources> {
    val data = run {
        val firstWithIds = first.data.associateBy(id)
        val secondWithIds = second.data.associateBy(id)

        val (firstOnly, firstIntersection) = firstWithIds.toList().partition { (id, _) -> id !in secondWithIds.keys }
        val (secondOnly, secondIntersection) = secondWithIds.toList().partition { (id, _) -> id !in firstWithIds.keys }
        val secondIntersectionMap = secondIntersection.toMap()
        val intersection = firstIntersection.map { (id, firstItem) ->
            val secondItem = secondIntersectionMap.getValue(id)
            onConflict(firstItem, secondItem)
        }

        firstOnly.map { it.second } + intersection + secondOnly.map { it.second }
    }
    return PagedData.Remote(
        data = data,
        paging = first.paging + second.paging
    )
}
fun <T> dualSourcesPagedDataOf(
    vararg items: T,
    paging: Paging.Page.DualSources = Paging.Page.DualSources.Initial
): PagedData.Remote<T, Paging.Page.DualSources> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> pagedDataOf(
    vararg items: T,
    paging: Paging.Page.SingleSource = Paging.Page.SingleSource.Initial
): PagedData.Remote<T, Paging.Page.SingleSource> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T, P : Paging.Page> pagedDataOf(
    vararg items: T,
    paging: P
): PagedData.Remote<T, P> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> List<T>.toPagedData(paging: Paging.Page) = PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData(paging: Paging.Page.SingleSource) = PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData(paging: Paging.Page.DualSources): PagedData.Remote<T, Paging.Page.DualSources> =
    PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData() = PagedData.Local(this)
