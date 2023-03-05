package tests

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.domain.usecase.LinkToTrakt
import cinescout.auth.domain.usecase.NotifyTraktAppAuthorized
import cinescout.movies.data.remote.trakt.testutil.TraktMoviesWatchlistJson
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.network.testutil.addHandler
import cinescout.network.testutil.respondJson
import cinescout.network.trakt.TraktAuthProvider
import cinescout.test.mock.junit5.MockAppExtension
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

    Given("app started") {
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

    Given("Trakt is linked") {
        mockAppExtension { newInstall() }
        authHelper.givenLinkedToTrakt()

        val getAllWatchlistMovies: GetAllWatchlistMovies by mockAppExtension.inject()
        val traktAuthProvider: TraktAuthProvider by mockAppExtension.inject()

        val initialAccessToken = traktAuthProvider.accessToken()
        initialAccessToken shouldNotBe null

        When("get watchlist") {

            And("token is expired (401)") {
                var callsCount = 0
                mockAppExtension.traktMockEngine.addHandler {
                    if ("oauth/token" in it.url.fullPath) error("")
                    if (callsCount++ == 0) respondError(HttpStatusCode.Unauthorized)
                    else respondJson(TraktMoviesWatchlistJson.OneMovie)
                }

                Then("watchlist is fetched") {
                    getAllWatchlistMovies().test {
                        awaitItem() shouldBe listOf(MovieSample.Inception).right()
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
