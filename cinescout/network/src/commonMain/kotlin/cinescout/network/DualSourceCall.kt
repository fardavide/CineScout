package cinescout.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.coroutineScope
import store.PagedData
import store.Paging
import store.builder.mergePagedData
import store.builder.toPagedData

suspend inline fun dualSourceCall(
    crossinline firstSourceCall: suspend () -> Either<NetworkOperation, Unit>,
    crossinline secondSourceCall: suspend () -> Either<NetworkOperation, Unit>
): Either<NetworkError, Unit> = coroutineScope {
    firstSourceCall()
        .tapLeft { if (it is NetworkOperation.Error) return@coroutineScope it.error.left() }
    secondSourceCall()
        .tapLeft { if (it is NetworkOperation.Error) return@coroutineScope it.error.left() }

    return@coroutineScope Unit.right()
}

suspend inline fun <T> dualSourceCallWithResult(
    crossinline firstSourceCall: suspend () -> Either<NetworkOperation, T>,
    crossinline secondSourceCall: suspend () -> Either<NetworkOperation, T>,
    crossinline merge: (first: T, second: T) -> T = { a, _ -> a }
): Either<NetworkOperation, T> = coroutineScope {
    val fromFirstSource = firstSourceCall()
        .tapLeft { if (it !is NetworkOperation.Skipped) return@coroutineScope it.left() }
    val fromSecondSource = secondSourceCall()
        .tapLeft { if (it !is NetworkOperation.Skipped) return@coroutineScope it.left() }

    val first = fromFirstSource.orNull()
    val second = fromSecondSource.orNull()
    when {
        first != null && second != null -> merge(first, second).right()
        first != null -> first.right()
        second != null -> second.right()
        else -> NetworkOperation.Skipped.left()
    }
}

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
        first != null -> first.data.toPagedData(
            Paging.Page.DualSources(
                first.paging,
                Paging.Page.SingleSource.Initial
            )
        ).right()

        second != null -> second.data.toPagedData(
            Paging.Page.DualSources(
                Paging.Page.SingleSource.Initial,
                second.paging
            )
        ).right()

        else -> NetworkOperation.Skipped.left()
    }
}
