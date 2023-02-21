package cinescout.auth.tmdb.domain.usecase

import arrow.core.left
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.tmdb.domain.FakeTmdbAccountRepository
import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import store.Refresh

class RealUnlinkFromTmdbTest : BehaviorSpec({

    Given("there is a linked account") {
        val tmdbAccountRepository = FakeTmdbAccountRepository(account = AccountSample.Tmdb)
        val tmdbAuthRepository = FakeTmdbAuthRepository()
        val unlink = RealUnlinkFromTmdb(
            tmdbAccountRepository = tmdbAccountRepository,
            tmdbAuthRepository = tmdbAuthRepository
        )

        When("unlink is called") {
            unlink()

            Then("the service is unlinked") {
                tmdbAuthRepository.isLinked().first() shouldBe false
            }

            Then("the account is removed") {
                tmdbAccountRepository.getAccount(Refresh.IfNeeded)
                    .first() shouldBe GetAccountError.NotConnected.left()
            }
        }
    }
})
