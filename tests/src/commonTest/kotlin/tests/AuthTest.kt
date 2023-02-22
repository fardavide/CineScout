package tests

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.tmdb.data.remote.BaseTmdbAuthorizeTokenUrl
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.test.mock.junit5.MockAppExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.test.inject

class AuthTest : BehaviorSpec({
    val mockAppExtension = MockAppExtension {
        appScope(TestScope(UnconfinedTestDispatcher()))
    }
    extensions(mockAppExtension)

    Given("app started") {
        val linkToTmdb: LinkToTmdb by mockAppExtension.inject()
        val linkToTrakt: LinkToTrakt by mockAppExtension.inject()
        val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized by mockAppExtension.inject()
        val notifyTraktAppAuthorized: NotifyTraktAppAuthorized by mockAppExtension.inject()

        When("link to Tmdb") {
            linkToTmdb().test {

                Then("success") {
                    val authorizationUrl = "$BaseTmdbAuthorizeTokenUrl${TmdbRequestTokenSample.RequestToken.value}"
                    awaitItem() shouldBe LinkToTmdb.State.UserShouldAuthorizeToken(authorizationUrl).right()
                    notifyTmdbAppAuthorized()
                    awaitItem() shouldBe LinkToTmdb.State.Success.right()
                }
            }
        }

        When("link to Trakt") {
            linkToTrakt().test {

                Then("success") {
                    awaitItem().getOrNull().shouldBeInstanceOf<LinkToTrakt.State.UserShouldAuthorizeApp>()
                    notifyTraktAppAuthorized(TraktAuthorizationCodeSample.AuthorizationCode)
                    awaitItem() shouldBe LinkToTrakt.State.Success.right()
                }
            }
        }
    }
})
