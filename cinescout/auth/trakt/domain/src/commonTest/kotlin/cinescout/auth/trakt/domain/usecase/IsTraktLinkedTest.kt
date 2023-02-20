package cinescout.auth.trakt.domain.usecase

import app.cash.turbine.test
import cinescout.auth.trakt.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class IsTraktLinkedTest : BehaviorSpec({

    Given("is linked in repository") {
        val repository = FakeTraktAuthRepository(isLinked = true)
        val isLinked = RealIsTraktLinked(repository)

        When("use case is called") {
            isLinked().test {

                Then("it returns true") {
                    awaitItem() shouldBe true
                }
            }
        }
    }

    Given("is not linked in repository") {
        val repository = FakeTraktAuthRepository(isLinked = false)
        val isLinked = RealIsTraktLinked(repository)

        When("use case is called") {
            isLinked().test {

                Then("it returns false") {
                    awaitItem() shouldBe false
                }
            }
        }
    }
})
