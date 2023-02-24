package cinescout.auth.trakt.data

import cinescout.auth.trakt.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealRefreshTraktAccessTokenTest : BehaviorSpec({

    Given("refresh access token") {
        val scenario = TestScenario()

        When("refreshing access token") {
            scenario.sut()

            Then("access token should be refreshed in the repository") {
                scenario.authRepository.refreshAccessTokenInvoked shouldBe true
            }
        }
    }
})

private class RealRefreshTraktAccessTokenTestScenario(
    val sut: RealRefreshTraktAccessToken,
    val authRepository: FakeTraktAuthRepository
)

private fun TestScenario(): RealRefreshTraktAccessTokenTestScenario {
    val authRepository = FakeTraktAuthRepository()
    return RealRefreshTraktAccessTokenTestScenario(
        sut = RealRefreshTraktAccessToken(
            authRepository = authRepository
        ),
        authRepository
    )
}
