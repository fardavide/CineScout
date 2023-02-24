package store.builder

import store.PagedData
import store.Paging

fun <T> localPagedDataOf(vararg items: T): PagedData.Local<T> = PagedData.Local(items.toList())
fun <T> remotePagedDataOf(vararg items: T, paging: Paging.Page = Paging.Page.Initial): PagedData.Remote<T> =
    PagedData.Remote(data = items.toList(), paging = paging)
fun <T> List<T>.toLocalPagedData() = PagedData.Local(this)
fun <T> List<T>.toRemotePagedData(paging: Paging.Page): PagedData.Remote<T> = PagedData.Remote(this, paging)
