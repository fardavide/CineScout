package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.FakeTmdbAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class UnlinkFromTmdbTest : BehaviorSpec({

    Given("a use case to unlink from TMDb") {
        val tmdbAuthRepository = FakeTmdbAuthRepository()
        val unlink = UnlinkFromTmdb(tmdbAuthRepository)

        When("it's executed") {
            unlink()

            Then("the account is unlinked") {
                tmdbAuthRepository.isLinked().first() shouldBe false
            }
        }
    }
})
