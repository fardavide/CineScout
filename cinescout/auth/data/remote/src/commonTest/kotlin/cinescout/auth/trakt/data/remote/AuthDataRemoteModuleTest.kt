// NoDefinitionFoundException
@file:Suppress("TYPEALIAS_EXPANSION_DEPRECATION")

package cinescout.auth.trakt.data.remote

import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktClientId
import cinescout.network.trakt.TraktClientSecret
import cinescout.network.trakt.TraktRedirectUrl
import cinescout.test.kotlin.KoinTestExtension
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.koin.core.error.NoDefinitionFoundException
import org.koin.core.qualifier.named
import org.koin.ksp.generated.module

class AuthDataRemoteModuleTest : BehaviorSpec({
    val koinExtension = extension(
        KoinTestExtension.create {
            modules(AuthDataRemoteModule().module)
        }
    )

    Given("AuthDataRemoteModule") {

        When("get a non qualified String") {
            val string = { koinExtension.koin.get<String>() }

            Then("it should throw") {
                shouldThrow<NoDefinitionFoundException>(string)
            }
        }

        When("get client id") {
            val string = koinExtension.koin.get<String>(named(TraktClientId))

            Then("it should be correct") {
                string shouldBe TRAKT_CLIENT_ID
            }
        }

        When("get client secret") {
            val string = koinExtension.koin.get<String>(named(TraktClientSecret))

            Then("it should be correct") {
                string shouldBe TRAKT_CLIENT_SECRET
            }
        }

        When("get redirect url") {
            val string = koinExtension.koin.get<String>(named(TraktRedirectUrl))

            Then("it should be correct") {
                string shouldBe TraktRedirectUrlString
            }
        }
    }
})
