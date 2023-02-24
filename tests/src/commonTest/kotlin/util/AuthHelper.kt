package util

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.tmdb.domain.usecase.UnlinkFromTmdb
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.UnlinkFromTrakt
import io.kotest.matchers.types.shouldBeInstanceOf
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthHelper : KoinTest {

    suspend fun givenLinkedToTmdb() {
        val unlinkFromTrakt: UnlinkFromTrakt by inject()
        unlinkFromTrakt()

        val expectedSuccess = LinkToTmdb.State.Success.right()
        val linkToTmdb: LinkToTmdb by inject()
        val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized by inject()
        var hasFinished = false

        linkToTmdb().test {

            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizationStateEither)
            val authorizationState = authorizationStateEither.value
            if (authorizationState is LinkToTmdb.State.UserShouldAuthorizeToken) {
                println(authorizationState.authorizationUrl)
                notifyTmdbAppAuthorized()
                assertEquals(expectedSuccess, awaitItem())
            }
            hasFinished = true
        }

        while (hasFinished.not()) {
            // wait for the auth to finish
        }
    }

    suspend fun givenLinkedToTrakt() {
        val unlinkFromTmdb: UnlinkFromTmdb by inject()
        unlinkFromTmdb()

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
