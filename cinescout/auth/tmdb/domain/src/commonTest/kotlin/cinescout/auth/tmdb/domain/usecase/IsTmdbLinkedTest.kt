package cinescout.auth.tmdb.domain.usecase

import app.cash.turbine.test
import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class IsTmdbLinkedTest : BehaviorSpec({

    Given("is linked in repository") {
        val repository = FakeTmdbAuthRepository(isLinked = true)
        val isLinked = RealIsTmdbLinked(repository)

        When("use case is called") {
            isLinked().test {

                Then("it returns true") {
                    awaitItem() shouldBe true
                }
            }
        }
    }

    Given("is not linked in repository") {
        val repository = FakeTmdbAuthRepository(isLinked = false)
        val isLinked = RealIsTmdbLinked(repository)

        When("use case is called") {
            isLinked().test {

                Then("it returns false") {
                    awaitItem() shouldBe false
                }
            }
        }
    }
})
