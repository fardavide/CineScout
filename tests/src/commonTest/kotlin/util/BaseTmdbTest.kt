package util

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import kotlinx.coroutines.delay
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

interface BaseTmdbTest : KoinTest {

    suspend fun givenSuccessfullyLinkedToTmdb() {
        // given
        val expectedSuccess = LinkToTmdb.State.Success.right()
        val linkToTmdb: LinkToTmdb = get()
        val notifyAppAuthorized: NotifyTmdbAppAuthorized = get()
        var hasFinished = false

        // when
        linkToTmdb().test {

            // then
            val authorizationStateEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizationStateEither)
            val authorizationState = authorizationStateEither.value
            if (authorizationState is LinkToTmdb.State.UserShouldAuthorizeToken) {
                println(authorizationState.authorizationUrl)
                delay(5.toDuration(SECONDS))
                notifyAppAuthorized()
                assertEquals(expectedSuccess, awaitItem())
            }
            hasFinished = true
        }

        while (hasFinished.not()) {
            // wait for the auth to finish
        }
    }
}
