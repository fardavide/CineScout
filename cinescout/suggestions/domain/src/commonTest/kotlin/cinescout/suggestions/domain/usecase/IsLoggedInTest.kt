package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import cinescout.auth.domain.usecase.FakeIsTmdbLinked
import cinescout.auth.domain.usecase.FakeIsTraktLinked
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class IsLoggedInTest : BehaviorSpec({

    Given("Tmdb is linked") {
        val isTmdbLinked = true

        When("Trakt is not linked") {
            val scenario = TestScenario(
                isTmdbLinked = isTmdbLinked,
                isTraktLinked = false
            )

            Then("emits true") {
                scenario.sut().test {
                    awaitItem() shouldBe true
                    awaitComplete()
                }
            }
        }

        When("Trakt is linked") {
            val scenario = TestScenario(
                isTmdbLinked = isTmdbLinked,
                isTraktLinked = true
            )

            Then("throws exception") {
                shouldThrowWithMessage<IllegalStateException>(
                    message = "Both accounts are connected: this is not supported"
                ) { scenario.sut().first() }
            }
        }
    }

    Given("Trakt is linked") {
        val isTraktLinked = true

        When("Tmdb is not linked") {
            val scenario = TestScenario(
                isTmdbLinked = false,
                isTraktLinked = isTraktLinked
            )

            Then("emits true") {
                scenario.sut().test {
                    awaitItem() shouldBe true
                    awaitComplete()
                }
            }
        }

        When("Tmdb is linked") {
            val scenario = TestScenario(
                isTmdbLinked = true,
                isTraktLinked = isTraktLinked
            )

            Then("throws exception") {
                shouldThrowWithMessage<IllegalStateException>(
                    message = "Both accounts are connected: this is not supported"
                ) { scenario.sut().first() }
            }
        }
    }

    Given("Tmdb and Trakt are not linked") {
        val scenario = TestScenario(
            isTmdbLinked = false,
            isTraktLinked = false
        )

        Then("emits false") {
            scenario.sut().test {
                awaitItem() shouldBe false
                awaitComplete()
            }
        }
    }
})

private class IsLoggedInTestScenario(
    val sut: IsLoggedIn
)

private fun TestScenario(isTmdbLinked: Boolean, isTraktLinked: Boolean) = IsLoggedInTestScenario(
    sut = IsLoggedIn(
        isTmdbLinked = FakeIsTmdbLinked(isLinked = isTmdbLinked),
        isTraktLinked = FakeIsTraktLinked(isLinked = isTraktLinked)
    )
)
