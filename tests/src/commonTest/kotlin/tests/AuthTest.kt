package tests

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.domain.usecase.LinkToTrakt
import cinescout.auth.domain.usecase.NotifyTraktAppAuthorized
import cinescout.network.testutil.addHandler
import cinescout.network.testutil.respondJson
import cinescout.network.trakt.TraktAuthProvider
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.test.mock.junit5.MockAppExtension
import cinescout.watchlist.data.remote.res.TraktWatchlistMetadataJson
import cinescout.watchlist.domain.usecase.GetWatchlistIds
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.test.inject
import util.AuthHelper

class AuthTest : BehaviorSpec({
    val mockAppExtension = MockAppExtension {
        appScope(TestScope(UnconfinedTestDispatcher()))
    }
    extensions(mockAppExtension)
    val authHelper = AuthHelper()

    xGiven("app started") {
        val linkToTrakt: LinkToTrakt by mockAppExtension.inject()
        val notifyTraktAppAuthorized: NotifyTraktAppAuthorized by mockAppExtension.inject()

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

    xGiven("Trakt is linked") {
        mockAppExtension { newInstall() }
        authHelper.givenLinkedToTrakt()

        val getWatchlistIds: GetWatchlistIds by mockAppExtension.inject()
        val traktAuthProvider: TraktAuthProvider by mockAppExtension.inject()

        val initialAccessToken = traktAuthProvider.accessToken()
        initialAccessToken shouldNotBe null

        xWhen("get watchlist") {

            And("token is expired (401)") {
                var callsCount = 0
                mockAppExtension.traktMockEngine.addHandler {
                    if ("oauth/token" in it.url.fullPath) error("")
                    if (callsCount++ == 0) respondError(HttpStatusCode.Unauthorized)
                    else respondJson(TraktWatchlistMetadataJson.OneMovie)
                }

                Then("watchlist is fetched") {
                    getWatchlistIds(ScreenplayType.All, refresh = false).test {
                        awaitItem().dataOrNull() shouldBe listOf(TmdbScreenplayIdSample.Inception).right()
                        cancelAndIgnoreRemainingEvents()
                    }
                }

                Then("token is refreshed") {
                    traktAuthProvider.accessToken() shouldNotBe initialAccessToken
                }
            }
        }
    }
})
