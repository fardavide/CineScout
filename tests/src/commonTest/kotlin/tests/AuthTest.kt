package tests

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.auth.trakt.domain.testdata.TraktTestData
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.network.testutil.plus
import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.CineScoutTmdbV4Client
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktNetworkQualifier
import kotlinx.coroutines.test.runTest
import org.koin.dsl.module
import org.koin.test.inject
import util.BaseAppTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthTest : BaseAppTest() {

    private val linkToTmdb: LinkToTmdb by inject()
    private val linkToTrakt: LinkToTrakt by inject()
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized by inject()
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized by inject()

    override val extraModule = module {
        factory(TmdbNetworkQualifier.V3.Client) {
            CineScoutTmdbV3Client(
                engine = MockTmdbAuthEngine() + MockTmdbAccountEngine(),
                authProvider = get()
            )
        }
        factory(TmdbNetworkQualifier.V4.Client) {
            CineScoutTmdbV4Client(
                engine = MockTmdbAuthEngine(),
                authProvider = get()
            )
        }
        factory(TraktNetworkQualifier.Client) {
            CineScoutTraktClient(
                authProvider = get()
                engine = MockTraktAccountEngine() + MockTraktAuthEngine()
            )
        }
    }

    @Test
    fun `link to Tmdb`() = runTest {

        // given
        val expectedSuccess = LinkToTmdb.State.Success.right()

        // when
        linkToTmdb().test {

            // then
            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State.UserShouldAuthorizeToken>>(authorizationStateEither)
            notifyTmdbAppAuthorized()

            assertEquals(expectedSuccess, awaitItem())
        }
    }

    @Test
    fun `link to Trakt`() = runTest {

        // given
        val expectedSuccess = LinkToTrakt.State.Success.right()

        // when
        linkToTrakt().test {

            // then
            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State.UserShouldAuthorizeApp>>(authorizationStateEither)
            notifyTraktAppAuthorized(TraktTestData.AuthorizationCode)

            assertEquals(expectedSuccess, awaitItem())
        }
    }
}
