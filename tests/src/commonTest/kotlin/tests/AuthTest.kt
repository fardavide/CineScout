package tests

import app.cash.turbine.test
import arrow.core.right
import cinescout.auth.tmdb.data.remote.BaseTmdbAuthorizeTokenUrl
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.movies.data.remote.trakt.testutil.TraktMoviesWatchlistJson
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.network.testutil.setHandler
import cinescout.network.trakt.testutil.respondTraktJsonPage
import cinescout.test.mock.MockEngines
import cinescout.test.mock.junit5.MockAppExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.test.inject
import store.builder.localPagedDataOf
import util.AuthHelper

class AuthTest : BehaviorSpec({
    val mockAppExtension = MockAppExtension {
        appScope(TestScope(UnconfinedTestDispatcher()))
    }
    extensions(mockAppExtension)
    val authHelper = AuthHelper()

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

    Given("Trakt is linked") {
        mockAppExtension { newInstall() }
        authHelper.givenLinkedToTrakt()

        val getAllWatchlistMovies: GetAllWatchlistMovies by mockAppExtension.inject()
        val traktAuthLocalDataSource: TraktAuthLocalDataSource by mockAppExtension.inject()

        val initialAccessToken = traktAuthLocalDataSource.findTokens()?.accessToken
        initialAccessToken shouldNotBe null

        When("get watchlist") {

            And("token is expired (401)") {
                var callsCount = 0
                MockEngines.trakt.movie.setHandler {
                    if (callsCount++ == 0) respondError(HttpStatusCode.Unauthorized)
                    else respondTraktJsonPage(TraktMoviesWatchlistJson.OneMovie)
                }

                Then("watchlist is fetched") {
                    getAllWatchlistMovies().test {
                        awaitItem() shouldBe localPagedDataOf(MovieSample.Inception).right()
                    }
                }

                Then("token is refreshed") {
                    traktAuthLocalDataSource.findTokens()?.accessToken shouldNotBe initialAccessToken
                }
            }
        }
    }
})
