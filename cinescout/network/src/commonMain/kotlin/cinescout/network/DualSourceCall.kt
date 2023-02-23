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
    page: Paging.Page,
    @Suppress("MaxLineLength")
    crossinline firstSourceCall: suspend (page: Paging.Page) -> Either<NetworkOperation, PagedData.Remote<T>>,
    @Suppress("MaxLineLength")
    crossinline secondSourceCall: suspend (page: Paging.Page) -> Either<NetworkOperation, PagedData.Remote<T>>
): Either<NetworkOperation, PagedData.Remote<T>> = coroutineScope {
    val firstSourceResult =
        if (page.isValid()) firstSourceCall(page)
        else NetworkOperation.Skipped.left()
    val secondSourceResult =
        if (page.isValid()) secondSourceCall(page)
        else NetworkOperation.Skipped.left()

    firstSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }
    secondSourceResult.onLeft { if (it is NetworkOperation.Error) return@coroutineScope it.left() }

    firstSourceResult.onRight { pagedData ->
        return@coroutineScope PagedData.Remote(
            pagedData.data,
            paging = page
        ).right()
    }
    secondSourceResult.onRight { pagedData ->
        return@coroutineScope PagedData.Remote(
            pagedData.data,
            paging = page
        ).right()
    }

    return@coroutineScope NetworkOperation.Skipped.left()
}

