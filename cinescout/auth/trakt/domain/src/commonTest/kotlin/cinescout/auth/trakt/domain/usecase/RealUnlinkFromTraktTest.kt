package cinescout.auth.trakt.domain.usecase

import arrow.core.left
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.FakeTraktAccountRepository
import cinescout.account.trakt.domain.sample.TraktAccountSample
import cinescout.auth.trakt.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import store.Refresh

class RealUnlinkFromTraktTest : BehaviorSpec({

    Given("there is a linked account") {
        val traktAccountRepository = FakeTraktAccountRepository(account = TraktAccountSample.Account)
        val traktAuthRepository = FakeTraktAuthRepository()
        val unlink = RealUnlinkFromTrakt(
            traktAccountRepository = traktAccountRepository,
            traktAuthRepository = traktAuthRepository
        )

        When("unlink is called") {
            unlink()

            Then("the service is unlinked") {
                traktAuthRepository.isLinked().first() shouldBe false
            }

            Then("the account is removed") {
                traktAccountRepository.getAccount(Refresh.IfNeeded)
                    .first() shouldBe GetAccountError.NoAccountConnected.left()
            }
        }
    }
})

