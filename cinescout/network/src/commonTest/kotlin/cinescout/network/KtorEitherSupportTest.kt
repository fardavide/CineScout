package cinescout.network

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.SocketTimeoutException
import io.ktor.client.request.get
import kotlinx.coroutines.test.runTest
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorEitherSupportTest {

    @Test
    fun `handles ConnectException`() = runTest {
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
    fun `handles SocketTimeoutException`() = runTest {
        // given
        val expected = NetworkError.NoNetwork.left()
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
    fun `handles UnknownHostException`() = runTest {
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
}
