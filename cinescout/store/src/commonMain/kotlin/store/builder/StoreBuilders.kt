package store.builder

import arrow.core.Either
import arrow.core.right
import cinescout.error.DataError
import kotlinx.coroutines.flow.flowOf
import store.Store
import store.StoreImpl

fun <T> storeOf(vararg items: T): Store<T> =
    storeOf(*items.map { it.right() }.toTypedArray())

fun <T> storeOf(vararg items: Either<DataError, T>): Store<T> =
    StoreImpl(flow = flowOf(*items))
