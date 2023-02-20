package cinescout.auth.trakt.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.trakt.domain.FakeTraktAccountRepository
import cinescout.auth.trakt.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealLinkToTraktTest : BehaviorSpec({

    Given("linking to Trakt") {
        val traktAccountRepository = FakeTraktAccountRepository()
        val traktAuthRepository = FakeTraktAuthRepository()
        val linkToTrakt = RealLinkToTrakt(
            traktAccountRepository = traktAccountRepository,
            traktAuthRepository = traktAuthRepository
        )

        When("linking is successful") {
            linkToTrakt().test {

                Then("it emits success") {
                    awaitItem() shouldBe LinkToTrakt.State.Success.right()
                }

                Then("it sync the account") {
                    traktAccountRepository.didSyncAccount shouldBe true
                }

                awaitComplete()
            }
        }
    }
})
