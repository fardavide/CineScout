package cinescout.auth.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealLinkToTraktTest : BehaviorSpec({

    Given("linking to Trakt") {
        val traktAuthRepository = FakeTraktAuthRepository()
        val linkToTrakt = RealLinkToTrakt(
            traktAuthRepository = traktAuthRepository
        )

        When("linking is successful") {
            linkToTrakt().test {

                Then("it emits success") {
                    awaitItem() shouldBe LinkToTrakt.State.Success.right()
                }

                awaitComplete()
            }
        }
    }
})
