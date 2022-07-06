package cinescout.network

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import cinescout.error.NetworkError
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.*
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.statement.*

/**
 * Catch [KtorEitherException] for create an [Either]
 * @return [Either] of [NetworkError] and [B]
 */
inline fun <B> Either.Companion.Try(block: () -> B): Either<NetworkError, B> =
    try {
        Right(block())
    } catch (e: KtorEitherException) {
        Left(e.reason)
    }

fun HttpClientConfig<*>.withEitherValidator() =
    HttpResponseValidator {
        validateResponse { response ->
            val error = when (response.status.value) {
                401 -> NetworkError.Unauthorized
                403 -> NetworkError.Forbidden
                404 -> NetworkError.NotFound
                500 -> NetworkError.Internal
                else -> null
            }
            error?.let { throw KtorEitherException(it) }
        }
    }

@PublishedApi
internal class KtorEitherException(val reason: NetworkError) : Throwable(reason.toString())
