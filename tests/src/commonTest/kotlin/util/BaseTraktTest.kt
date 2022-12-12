package util

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.domain.testdata.TraktTestData
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import kotlinx.coroutines.delay
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

interface BaseTraktTest : KoinTest {

    suspend fun givenSuccessfullyLinkedToTrakt() {
        // given
        val expectedSuccess = LinkToTrakt.State.Success.right()
        val linkToTrakt: LinkToTrakt = get()
        val notifyAppAuthorized: NotifyTraktAppAuthorized = get()
        var hasFinished = false

        // when
        linkToTrakt().test {

            // then
            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State>>(authorizationStateEither)
            val authorizationState = authorizationStateEither.value
            if (authorizationState is LinkToTrakt.State.UserShouldAuthorizeApp) {
                println(authorizationState.authorizationUrl)
                delay(5.toDuration(SECONDS))
                notifyAppAuthorized(TraktTestData.AuthorizationCode)
                assertEquals(expectedSuccess, awaitItem())
            }
            hasFinished = true
        }

        while (hasFinished.not()) {
            // wait for the auth to finish
        }
    }
}
