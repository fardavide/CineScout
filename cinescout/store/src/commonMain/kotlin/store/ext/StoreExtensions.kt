package store.ext

import arrow.core.Either
import cinescout.error.DataError
import kotlinx.coroutines.flow.map
import store.Store
import store.StoreImpl

fun <T, R> Store<T>.map(
    transform: (Either<DataError, T>) -> Either<DataError.Remote, R>
): Store<R> = with(this as StoreImpl<T>) {
    StoreImpl(flow.map(transform))
}
