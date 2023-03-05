package store.builder

import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import kotlinx.coroutines.flow.flowOf
import store.Store
import store.StoreImpl

fun <T> emptyListStore(): Store<List<T>> = storeOf(emptyList())

fun <T> storeOf(vararg items: T): Store<T> =
    StoreImpl(flow = flowOf(*items.map { it.right() }.toTypedArray()))

fun <T> storeOf(dataError: DataError): Store<T> = StoreImpl(flow = flowOf(dataError.left()))
