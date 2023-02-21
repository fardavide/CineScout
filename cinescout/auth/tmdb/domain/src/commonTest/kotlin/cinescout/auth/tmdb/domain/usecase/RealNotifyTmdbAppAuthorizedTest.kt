package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealNotifyTmdbAppAuthorizedTest : BehaviorSpec({

    Given("use case") {
        val authRepository = FakeTmdbAuthRepository()
        val notify = RealNotifyTmdbAppAuthorized(authRepository)

        When("it is called") {
            notify()

            Then("it calls the repository") {
                authRepository.didNotifyTokenAuthorized shouldBe true
            }
        }
    }
})