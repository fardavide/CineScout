package cinescout.network

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondError
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class KtorEitherSupportTest : BehaviorSpec({

    Given("converting to Either") {

        When("ConnectionException in thrown") {
            val scenario = TestScenario { throw ConnectException("host") }

            Then("NoNetwork is returned") {
                scenario.execute() shouldBe NetworkError.NoNetwork.left()
            }
        }

        When("SocketException is thrown") {
            val scenario = TestScenario { throw SocketException("Software caused connection abort") }

            Then("Unreachable is returned") {
                scenario.execute() shouldBe NetworkError.Unreachable.left()
            }
        }

        When("SocketTimeoutException is thrown") {
            val scenario = TestScenario { throw SocketTimeoutException("Read timed out") }

            Then("Unreachable is returned") {
                scenario.execute() shouldBe NetworkError.Unreachable.left()
            }
        }

        When("SSLHandshakeException is thrown") {
            val scenario = TestScenario { throw SSLHandshakeException("connection closed") }

            Then("NoNetwork is returned") {
                scenario.execute() shouldBe NetworkError.NoNetwork.left()
            }
        }

        When("UnknownHostException is thrown") {
            val scenario = TestScenario { throw UnknownHostException("host") }

            Then("NoNetwork is returned") {
                scenario.execute() shouldBe NetworkError.NoNetwork.left()
            }
        }

        When("service unavailable error") {
            val scenario = TestScenario { _ ->
                respondError(HttpStatusCode.ServiceUnavailable)
            }

            Then("Unreachable is returned") {
                scenario.execute() shouldBe NetworkError.Unreachable.left()
            }
        }
    }
})

private class KtorEitherSupportTestScenario(
    private val client: HttpClient
) {
    
    suspend fun execute(): Either<NetworkError, String> = Either.Try { client.get("url").body() }
}

private fun TestScenario(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) =
    KtorEitherSupportTestScenario(
        client = CineScoutClient(engine = MockEngine(handler))
    )

