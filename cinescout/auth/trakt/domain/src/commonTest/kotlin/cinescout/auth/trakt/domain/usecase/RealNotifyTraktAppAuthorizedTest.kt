package cinescout.auth.trakt.domain.usecase

import cinescout.auth.trakt.domain.FakeTraktAuthRepository
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealNotifyTraktAppAuthorizedTest : BehaviorSpec({

    Given("use case") {
        val authRepository = FakeTraktAuthRepository()
        val notify = RealNotifyTraktAppAuthorized(authRepository)

        When("it is called") {
            val code = TraktAuthorizationCodeSample.AuthorizationCode
            notify(code)

            Then("it calls the repository") {
                authRepository.notifyAppAuthorizedCodeInvoked shouldBe code
            }
        }
    }
})
