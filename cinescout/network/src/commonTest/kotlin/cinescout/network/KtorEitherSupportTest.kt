package cinescout.network

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.SocketTimeoutException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorEitherSupportTest {

    @Test
    fun handles_ConnectException() = runTest {
        // given
        val expected = NetworkError.NoNetwork.left()
        val client = CineScoutClient(
            engine = MockEngine {
                throw ConnectException("host")
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun handles_SocketException() = runTest {
        // given
        val expected = NetworkError.Unreachable.left()
        val client = CineScoutClient(
            engine = MockEngine {
                throw SocketException("Software caused connection abort")
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun handles_SocketTimeoutException() = runTest {
        // given
        val expected = NetworkError.Unreachable.left()
        val client = CineScoutClient(
            engine = MockEngine { requestData ->
                throw SocketTimeoutException(requestData)
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun handles_SSLHandshakeException() = runTest {
        // given
        val expected = NetworkError.NoNetwork.left()
        val client = CineScoutClient(
            engine = MockEngine {
                throw SSLHandshakeException("connection closed")
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun handles_UnknownHostException() = runTest {
        // given
        val expected = NetworkError.NoNetwork.left()
        val client = CineScoutClient(
            engine = MockEngine {
                throw UnknownHostException("host")
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }

    @Test
    fun handles_service_unavailable() = runTest {
        // given
        val expected = NetworkError.Unreachable.left()
        val client = CineScoutClient(
            engine = MockEngine {
                respondError(HttpStatusCode.ServiceUnavailable)
            }
        )

        // when
        val result: Either<NetworkError, String> = Either.Try { client.get("url").body() }

        // then
        assertEquals(expected, result)
    }
}
