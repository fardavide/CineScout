package store.builder

import store.PagedData
import store.Paging

fun <T> dualSourcesPagedDataOf(
    vararg items: T,
    paging: Paging.Page = Paging.Page.Initial
): PagedData.Remote<T> = PagedData.Remote(data = items.toList(), paging = paging)
fun <T> pagedDataOf(vararg items: T, paging: Paging.Page = Paging.Page.Initial): PagedData.Remote<T> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> List<T>.toPagedData(paging: Paging.Page) = PagedData.Remote(this, paging)
fun <T> List<T>.toPagedData() = PagedData.Local(this)
