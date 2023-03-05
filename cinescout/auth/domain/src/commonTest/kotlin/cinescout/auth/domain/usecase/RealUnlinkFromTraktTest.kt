package cinescout.auth.domain.usecase

import cinescout.auth.domain.FakeTraktAuthRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class RealUnlinkFromTraktTest : BehaviorSpec({

    Given("there is a linked account") {
        val traktAuthRepository = FakeTraktAuthRepository()
        val unlink = RealUnlinkFromTrakt(
            traktAuthRepository = traktAuthRepository
        )

        When("unlink is called") {
            unlink()

            Then("the service is unlinked") {
                traktAuthRepository.isLinked().first() shouldBe false
            }
        }
    }
})

