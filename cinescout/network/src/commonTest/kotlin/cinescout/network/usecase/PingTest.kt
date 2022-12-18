package cinescout.network.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.network.CineScoutClient
import cinescout.network.testutil.setHandler
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PingTest {

    private val engine = MockEngine {
        respond("OK", HttpStatusCode.OK)
    }
    private val client = CineScoutClient(engine)
    private val ping = Ping(client)

    @Test
    fun when_network_call_succeed_returns_success() = runTest {
        // given
        val host = Ping.Host.Google
        val expected = Unit.right()

        // when
        val result = ping(host)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun when_network_call_fails_returns_error() = runTest {
        // given
        val host = Ping.Host.Google
        val expected = NetworkError.Unreachable.left()
        engine.setHandler {
            respondError(HttpStatusCode.ServiceUnavailable)
        }

        // when
        val result = ping(host)

        // then
        assertEquals(expected, result)
    }
}
