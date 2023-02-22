package cinescout.network.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.network.CineScoutClient
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode

class PingTest : BehaviorSpec({

    Given("network call succeeds") {
        val scenario = TestScenario { respond("OK", HttpStatusCode.OK) }

        When("pinging google") {
            val result = scenario.sut(Ping.Host.Google)

            Then("it should return success") {
                result shouldBe Unit.right()
            }
        }
    }

    Given("network call fails") {
        val scenario = TestScenario { respondError(HttpStatusCode.ServiceUnavailable) }

        When("pinging google") {
            val result = scenario.sut(Ping.Host.Google)

            Then("it should return error") {
                result shouldBe NetworkError.Unreachable.left()
            }
        }
    }
})

private class PingTestScenario(
    val sut: Ping
)

private fun TestScenario(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) =
    PingTestScenario(
        sut = RealPing(CineScoutClient(MockEngine(handler)))
    )
