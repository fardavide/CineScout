package cinescout.auth.tmdb.domain.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.tmdb.domain.FakeTmdbAccountRepository
import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class LinkToTmdbTest : BehaviorSpec({

    Given("linking to Tmdb") {
        val tmdbAccountRepository = FakeTmdbAccountRepository()
        val tmdbAuthRepository = FakeTmdbAuthRepository()
        val linkToTmdb = LinkToTmdb(
            tmdbAccountRepository = tmdbAccountRepository,
            tmdbAuthRepository = tmdbAuthRepository
        )

        When("linking is successful") {
            linkToTmdb().test {

                Then("it emits success") {
                    awaitItem() shouldBe LinkToTmdb.State.Success.right()
                }

                Then("it sync the account") {
                    tmdbAccountRepository.didSyncAccount shouldBe true
                }

                awaitComplete()
            }
        }
    }
})
