package tests

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.network.CineScoutClient
import cinescout.network.tmdb.TmdbNetworkQualifier
import kotlinx.coroutines.test.runTest
import org.koin.dsl.module
import org.koin.test.inject
import util.BaseAppTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthTest : BaseAppTest() {

    private val linkToTmdb: LinkToTmdb by inject()

    override val extraModule = module {
        factory(TmdbNetworkQualifier.V3.Client) { CineScoutClient(MockTmdbAuthEngine()) }
        factory(TmdbNetworkQualifier.V4.Client) { CineScoutClient(MockTmdbAuthEngine()) }
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
            val authorizationState = authorizationStateEither.value
            authorizationState.authorizationResultChannel.send(LinkToTmdb.TokenAuthorized.right())

            assertEquals(expectedSuccess, awaitItem())
        }
    }
}
