package cinescout.network

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import store.PagedData
import store.Paging
import store.builder.mergePagedData
import store.builder.toPagedData

suspend inline fun <T : Any> dualSourceCallWithResult(
    page: Paging.Page.DualSources,
    crossinline firstSourceCall: suspend (
        page: Paging.Page.SingleSource
    ) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>,
    crossinline secondSourceCall: suspend (
        page: Paging.Page.SingleSource
    ) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>,
    crossinline id: (T) -> Any = { it },
    crossinline onConflict: (first: T, second: T) -> T = { a, _ -> a }
): Either<NetworkOperation, PagedData.Remote<T, Paging.Page.DualSources>> = dualSourceCallWithResult(
    page = page,
    firstSourceCall = firstSourceCall,
    secondSourceCall = secondSourceCall,
    merge = { a, b -> mergePagedData(first = a, second = b, id = id, onConflict = onConflict) }
)

suspend inline fun <T> dualSourceCallWithResult(
    page: Paging.Page.DualSources,
    crossinline firstSourceCall: suspend (
        page: Paging.Page.SingleSource
    ) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>,
    crossinline secondSourceCall: suspend (
        page: Paging.Page.SingleSource
    ) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>,
    crossinline merge: suspend (
        first: PagedData.Remote<T, Paging.Page.SingleSource>,
        second: PagedData.Remote<T, Paging.Page.SingleSource>
    ) -> PagedData.Remote<T, Paging.Page.DualSources>
): Either<NetworkOperation, PagedData.Remote<T, Paging.Page.DualSources>> = coroutineScope {
    val fromFirstSource = if (page.first.isValid()) {
        firstSourceCall(page.first)
            .tapLeft { if (it !is NetworkOperation.Skipped) return@coroutineScope it.left() }
    } else {
        NetworkOperation.Skipped.left()
    }
    val fromSecondSource = if (page.second.isValid()) {
        secondSourceCall(page.second)
            .tapLeft { if (it !is NetworkOperation.Skipped) return@coroutineScope it.left() }
    } else {
        NetworkOperation.Skipped.left()
    }

    val first = fromFirstSource.orNull()
    val second = fromSecondSource.orNull()
    when {
        first != null && second != null -> merge(first, second).right()
        first != null -> first.data.toPagedData(page).right()
        second != null -> second.data.toPagedData(page).right()
        else -> NetworkOperation.Skipped.left()
    }
}

class DualSourceCall(
    val isFirstSourceLinked: suspend () -> Boolean,
    val isSecondSourceLinked: suspend () -> Boolean
) {

    suspend inline operator fun invoke(
        crossinline firstSourceCall: suspend () -> Either<NetworkError, Unit>,
        crossinline secondSourceCall: suspend () -> Either<NetworkError, Unit>
    ): Either<NetworkError, Unit> =
        coroutineScope {
            val isFirstSourceLinked = isFirstSourceLinked()
            val isSecondSourceLinked = isSecondSourceLinked()
            if (isFirstSourceLinked.not() && isSecondSourceLinked.not()) {
                return@coroutineScope NetworkError.Unauthorized.left()
            }
            val firstSourceResult = async {
                if (isFirstSourceLinked) firstSourceCall()
                else Unit.right()
            }
            val secondSourceResult = async {
                if (isSecondSourceLinked) secondSourceCall()
                else Unit.right()
            }
            firstSourceResult.await().flatMap { secondSourceResult.await() }
        }
}
