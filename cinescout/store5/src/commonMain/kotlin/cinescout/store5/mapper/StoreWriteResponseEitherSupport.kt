package cinescout.store5.mapper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.store5.UpdateException
import org.mobilenativefoundation.store.store5.StoreWriteResponse

internal fun <Response : Any> StoreWriteResponse.toEither(): Either<NetworkError, Response> = when (this) {
    is StoreWriteResponse.Error.Message -> error("StoreWriteResponse.Error.Message is not supported")
    is StoreWriteResponse.Error.Exception -> {
        when (val responseError = error) {
            is UpdateException -> responseError.error.left()
            else -> error("StoreWriteResponse.Error.Exception is not UpdateException")
        }
    }
    is StoreWriteResponse.Success.Typed<*> -> @Suppress("UNCHECKED_CAST") (this.value as Response).right()
    is StoreWriteResponse.Success.Untyped -> @Suppress("UNCHECKED_CAST") (this.value as Response).right()
}
