package util

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import io.kotest.matchers.types.shouldBeInstanceOf
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class AuthHelper : KoinTest {

    suspend fun givenLinkedToTrakt() {
        val expectedSuccess = LinkToTrakt.State.Success.right()
        val linkToTrakt: LinkToTrakt by inject()
        val notifyTraktAppAuthorized: NotifyTraktAppAuthorized by inject()
        var hasFinished = false

        linkToTrakt().test {

            val authorizationStateEither = awaitItem()
            authorizationStateEither.shouldBeInstanceOf<Either.Right<LinkToTrakt.State>>()
            val authorizationState = authorizationStateEither.value
            if (authorizationState is LinkToTrakt.State.UserShouldAuthorizeApp) {
                println(authorizationState.authorizationUrl)
                notifyTraktAppAuthorized(TraktAuthorizationCodeSample.AuthorizationCode)
                assertEquals(expectedSuccess, awaitItem())
            }
            hasFinished = true
        }

        while (hasFinished.not()) {
            // wait for the auth to finish
        }
    }
}
