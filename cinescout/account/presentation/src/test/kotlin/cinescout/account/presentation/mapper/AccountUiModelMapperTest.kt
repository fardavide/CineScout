package cinescout.account.presentation.mapper

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class AccountUiModelMapperTest : BehaviorSpec({

    Given("trakt account") {
        val account = AccountSample.Trakt
        val gravatar = checkNotNull(account.gravatar)

        When("map to ui model") {
            val scenario = TestScenario()
            val result = scenario.sut.toUiModel(account)

            Then("image hash is correct") {
                result.imageUrl shouldContain gravatar.hash
            }

            Then("image size is large") {
                result.imageUrl shouldContain Gravatar.Size.LARGE.pixels.toString()
            }

            Then("username is correct") {
                result.username shouldBe account.username.value
            }
        }
    }
})

private class AccountUiModelMapperTestScenario(
    val sut: AccountUiModelMapper
)

private fun TestScenario() = AccountUiModelMapperTestScenario(
    sut = AccountUiModelMapper()
)
