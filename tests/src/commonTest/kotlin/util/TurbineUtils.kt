package util

import app.cash.turbine.ReceiveTurbine
import arrow.core.Either
import cinescout.error.DataError
import io.kotest.matchers.types.shouldBeInstanceOf
import store.PagedData
import store.Paging

/**
 * Awaits the first [PagedData.Remote] item of the [ReceiveTurbine] and returns its data
 */
suspend fun <T> ReceiveTurbine<Either<DataError, PagedData<T, Paging>>>.awaitRemoteData(): List<T> {
    var either = awaitItem()
    while (true) {
        either.shouldBeInstanceOf<Either.Right<PagedData<T, Paging>>>()
        val pagedData = either.value
        if (pagedData is PagedData.Remote) {
            return pagedData.data
        }
        either = awaitItem()
    }
}
