package util

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
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
        val linkToTmdb: LinkToTrakt = get()
        var hasFinished = false

        // when
        linkToTmdb().test {

            // then
            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State.UserShouldAuthorizeApp>>(authorizationStateEither)
            val authorizationState = authorizationStateEither.value
            println(authorizationState.authorizationUrl)
            delay(5.toDuration(SECONDS))
            authorizationState.authorizationResultChannel
                .send(LinkToTrakt.AppAuthorized(TraktAuthTestData.AuthorizationCode).right())

            assertEquals(expectedSuccess, awaitItem())
            awaitComplete()
            hasFinished = true
        }

        while (hasFinished.not()) {
            // wait for the auth to finish
        }
    }
}
