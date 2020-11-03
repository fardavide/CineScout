package network

import entities.Either
import entities.Left
import entities.NetworkError
import entities.Right
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpResponseValidator

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
internal class KtorEitherException(val reason: NetworkError): Throwable(reason.toString())
