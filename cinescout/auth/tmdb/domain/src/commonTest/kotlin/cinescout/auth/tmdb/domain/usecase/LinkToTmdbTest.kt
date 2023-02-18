package cinescout.auth.tmdb.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.tmdb.domain.usecase.SyncTmdbAccount
import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk

class LinkToTmdbTest : BehaviorSpec({

    Given("linking to TMDb") {
        val syncTmdbAccount: SyncTmdbAccount = mockk(relaxUnitFun = true)
        val tmdbAuthRepository = FakeTmdbAuthRepository()
        val linkToTmdb = LinkToTmdb(
            syncTmdbAccount = syncTmdbAccount,
            tmdbAuthRepository = tmdbAuthRepository
        )

        When("linking is successful") {
            linkToTmdb().test {

                Then("it emits success") {
                    awaitItem() shouldBe LinkToTmdb.State.Success.right()
                }

                Then("it sync the account") {
                    coVerify { syncTmdbAccount() }
                }

                awaitComplete()
            }
        }
    }
})
