package cinescout.profile.presentation.viewmodel

import app.cash.turbine.test
import cinescout.profile.presentation.state.ProfileState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ProfileViewModelTest : BehaviorSpec({

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("state is loading") {
                scenario.sut.state.test {
                    awaitItem() shouldBe ProfileState.Loading
                }
            }
        }
    }
})

private class ProfileViewModelTestScenario(
    val sut: ProfileViewModel
)

private fun TestScenario() = ProfileViewModelTestScenario(
    sut = ProfileViewModel()
)
