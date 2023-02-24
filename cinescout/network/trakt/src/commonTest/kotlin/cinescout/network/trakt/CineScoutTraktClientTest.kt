package cinescout.network.trakt

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode

class CineScoutTraktClientTest : BehaviorSpec({

    Given("a request") {

        When("response is authorized") {
            var i = 0
            val engine = MockEngine {
                if (i++ == 0) respondError(HttpStatusCode.Unauthorized)
                else respond("success", HttpStatusCode.OK)
            }
            val authProvider = FakeTraktAuthProvider()
            val client = CineScoutTraktClient(engine, authProvider)

            Then("token is refreshed") {
                val response: String = client.get { url("https://api.trakt.tv") }.body()
                response shouldBe "success"
                authProvider.refreshTokenRequestedCount shouldBe 2
            }
        }
    }
})
