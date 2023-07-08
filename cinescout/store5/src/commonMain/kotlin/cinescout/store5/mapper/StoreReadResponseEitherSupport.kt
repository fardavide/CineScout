package cinescout.store5.mapper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.store5.FetchException
import cinescout.store5.SkippedFetch
import cinescout.store5.Store5ReadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadResponse

internal fun <Output : Any> Flow<StoreReadResponse<Output>>.mapToStore5ReadResponse() =
    map { it.toStore5ReadResponse() }

internal fun <Output : Any> StoreReadResponse<Output>.toStore5ReadResponse(): Store5ReadResponse<Output> =
    when (this) {
        is StoreReadResponse.Data -> Store5ReadResponse.Data(value.right(), origin)
        is StoreReadResponse.Error -> requireResult().fold(
            ifLeft = { Store5ReadResponse.Data(it.left(), origin) },
            ifRight = { Store5ReadResponse.Skipped }
        )
        is StoreReadResponse.Loading -> Store5ReadResponse.Loading(origin)
        is StoreReadResponse.NoNewData -> Store5ReadResponse.Skipped
    }

private fun StoreReadResponse.Error.requireResult(): Either<NetworkError, NetworkOperation.Skipped> =
    when (this) {
        is StoreReadResponse.Error.Exception -> when (val responseError = error) {
            is FetchException -> responseError.networkError.left()
            is SkippedFetch -> NetworkOperation.Skipped.right()
            else -> throw RuntimeException("$responseError. Cause: ${responseError.cause}", responseError)
        }
        is StoreReadResponse.Error.Message -> error("StoreReadResponse.Error.Message is not supported")
    }
