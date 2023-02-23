package cinescout.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import kotlinx.coroutines.coroutineScope
import store.PagedData
import store.Paging

suspend inline fun dualSourceCall(
    crossinline firstSourceCall: suspend () -> Either<NetworkOperation, Unit>,
    crossinline secondSourceCall: suspend () -> Either<NetworkOperation, Unit>
): Either<NetworkError, Unit> = coroutineScope {
    firstSourceCall()
        .onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.error.left() }
    secondSourceCall()
        .onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.error.left() }

    return@coroutineScope Unit.right()
}

suspend inline fun <T> dualSourceCallWithResult(
    crossinline firstSourceCall: suspend () -> Either<NetworkOperation, T>,
    crossinline secondSourceCall: suspend () -> Either<NetworkOperation, T>
): Either<NetworkOperation, T> = coroutineScope {
    val firstSourceResult = firstSourceCall()
    val secondSourceResult = secondSourceCall()

    firstSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }
    secondSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }

    firstSourceResult.onRight { return@coroutineScope it.right() }
    secondSourceResult.onRight { return@coroutineScope it.right() }

    return@coroutineScope NetworkOperation.Skipped.left()
}

suspend inline fun <T> dualSourceCallWithResult(
    page: Paging.Page.DualSources,
    @Suppress("MaxLineLength")
    crossinline firstSourceCall: suspend (page: Paging.Page.SingleSource) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>,
    @Suppress("MaxLineLength")
    crossinline secondSourceCall: suspend (page: Paging.Page.SingleSource) -> Either<NetworkOperation, PagedData.Remote<T, Paging.Page.SingleSource>>
): Either<NetworkOperation, PagedData.Remote<T, Paging.Page.DualSources>> = coroutineScope {
    val firstSourceResult =
        if (page.first.isValid()) firstSourceCall(page.first)
        else NetworkOperation.Skipped.left()
    val secondSourceResult =
        if (page.second.isValid()) secondSourceCall(page.second)
        else NetworkOperation.Skipped.left()

    firstSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }
    secondSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }

    firstSourceResult.onRight { pagedData ->
        return@coroutineScope PagedData.Remote(
            pagedData.data,
            paging = Paging.Page.DualSources(pagedData.paging, pagedData.paging)
        ).right()
    }
    secondSourceResult.onRight { pagedData ->
        return@coroutineScope PagedData.Remote(
            pagedData.data,
            paging = Paging.Page.DualSources(pagedData.paging, pagedData.paging)
        ).right()
    }

    return@coroutineScope NetworkOperation.Skipped.left()
}

