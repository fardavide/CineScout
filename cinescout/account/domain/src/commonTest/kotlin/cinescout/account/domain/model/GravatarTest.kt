package cinescout.account.domain.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class GravatarTest : BehaviorSpec({

    Given("a Gravatar") {
        val gravatar = Gravatar("hash")

        When("getUrl is called with small size") {
            val size = Gravatar.Size.SMALL
            val url = gravatar.getUrl(size)

            Then("the url is correct") {
                url shouldBe "${Gravatar.BaseUrl}/hash?s=${size.pixels}.jpg"
            }
        }

        When("getUrl is called with medium size") {
            val size = Gravatar.Size.MEDIUM
            val url = gravatar.getUrl(size)

            Then("the url is correct") {
                url shouldBe "${Gravatar.BaseUrl}/hash?s=${size.pixels}.jpg"
            }
        }

        When("getUrl is called with large size") {
            val size = Gravatar.Size.LARGE
            val url = gravatar.getUrl(size)

            Then("the url is correct") {
                url shouldBe "${Gravatar.BaseUrl}/hash?s=${size.pixels}.jpg"
            }
        }
    }
})
