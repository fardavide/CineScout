package cinescout.auth.trakt.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealTraktAuthRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val localDataSource: TraktAuthLocalDataSource = mockk {
        val mutableAuthState = MutableStateFlow<TraktAuthState>(TraktAuthState.Idle)
        every { findAuthState() } returns mutableAuthState
        coEvery { storeAuthState(any()) } coAnswers { mutableAuthState.emit(firstArg()) }
    }
    private val remoteDataSource: TraktAuthRemoteDataSource = mockk {
        coEvery { createAccessToken(TraktAuthorizationCode(any())) } returns
            TraktAuthTestData.AccessAndRefreshToken.right()
        every { getAppAuthorizationUrl() } returns TraktAuthTestData.AppAuthorizationUrl
    }
    private val repository = RealTraktAuthRepository(
        dispatcher = dispatcher,
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

    @Test
    fun `is linked if auth state is completed`() = runTest {
        // given
        val expected = true
        every { localDataSource.findAuthState() } returns
            flowOf(TraktAuthState.Completed(TraktAuthTestData.AccessAndRefreshToken))

        // when
        val result = repository.isLinked()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `is not linked if auth state is idle`() = runTest {
        // given
        val expected = false
        every { localDataSource.findAuthState() } returns flowOf(TraktAuthState.Idle)

        // when
        val result = repository.isLinked()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `user authorized the request token`() = runTest {
        // given
        val expected = LinkToTrakt.State.Success.right()

        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTrakt.State.UserShouldAuthorizeApp>(authorizeItem)

            // when
            repository.notifyAppAuthorized(TraktAuthTestData.AuthorizationCode)

            // then
            assertEquals(expected, awaitItem())
            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                    findAuthState()
                    storeAuthState(TraktAuthState.AppAuthorized(TraktAuthTestData.AuthorizationCode))
                    storeAuthState(TraktAuthState.Completed(TraktAuthTestData.AccessAndRefreshToken))
                }
            }
        }
    }

    @Test
    fun `user did not authorize the request token`() = runTest {
        // given
        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTrakt.State.UserShouldAuthorizeApp>(authorizeItem)

            // when
            // nothing

            // then
            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                }
            }
        }
    }

    @Test
    fun `network error happened on access token`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        coEvery { remoteDataSource.createAccessToken(TraktAuthTestData.AuthorizationCode) } returns
            networkError.left()
        val expected = LinkToTrakt.Error.Network(networkError).left()

        // when
        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTrakt.State.UserShouldAuthorizeApp>(authorizeItem)

            // when
            repository.notifyAppAuthorized(TraktAuthTestData.AuthorizationCode)

            // then
            assertEquals(expected, awaitItem())
            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                    findAuthState()
                    storeAuthState(TraktAuthState.AppAuthorized(TraktAuthTestData.AuthorizationCode))
                }
            }
        }
    }
}
