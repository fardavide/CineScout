package cinescout.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Catch [KtorEitherException] for create an [Either]
 * @return [Either] of [NetworkError] and [B]
 */
inline fun <B> Either.Companion.Try(block: () -> B): Either<NetworkError, B> =
    runCatching { block() }.fold(
        onSuccess = { it.right() },
        onFailure = { throwable ->
            when (throwable) {
                is KtorEitherException -> throwable.reason.left()
                is ConnectException,
                is SSLHandshakeException,
                is UnknownHostException -> NetworkError.NoNetwork.left()
                is SocketException,
                is SocketTimeoutException -> NetworkError.Unreachable.left()
                else -> throw throwable
            }
        }
    )

fun HttpClientConfig<*>.withEitherValidator() =
    HttpResponseValidator {
        validateResponse { response ->
            val error = when (response.status.value) {
                401 -> NetworkError.Unauthorized
                403 -> NetworkError.Forbidden
                404 -> NetworkError.NotFound
                500 -> NetworkError.Internal
                502 -> NetworkError.Unreachable
                520 -> NetworkError.Unknown
                else -> null
            }
            error?.let { throw KtorEitherException(it) }
        }
    }

@PublishedApi
internal class KtorEitherException(val reason: NetworkError) : Throwable(reason.toString())
