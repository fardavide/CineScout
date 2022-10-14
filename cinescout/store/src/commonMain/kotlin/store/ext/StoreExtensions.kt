package store.ext

import arrow.core.Either
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import store.Store
import store.StoreImpl

fun <T, R> Store<T>.map(
    transform: (Either<DataError, T>) -> Either<DataError.Remote, R>
): Store<R> = with(this as StoreImpl<T>) {
    StoreImpl(flow.map(transform))
}

suspend fun <T> Store<T>.requireFirst(): Either<NetworkError, T> =
    filterNot { either -> either is Either.Left && either.value is DataError.Local }
        .map { either -> either.mapLeft { dataError -> (dataError as DataError.Remote).networkError } }
        .first()
