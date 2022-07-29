package cinescout.auth.tmdb.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealTmdbAuthRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val localDataSource: TmdbAuthLocalDataSource = mockk {
        val mutableAuthState = MutableStateFlow<TmdbAuthState>(TmdbAuthState.Idle)
        every { findAuthState() } returns mutableAuthState
        coEvery { storeAuthState(any()) } coAnswers { mutableAuthState.emit(firstArg()) }
    }
    private val remoteDataSource: TmdbAuthRemoteDataSource = mockk {
        coEvery { createRequestToken() } returns TmdbAuthTestData.RequestToken.right()
        coEvery { createAccessToken(TmdbAuthTestData.AuthorizedRequestToken) } returns TmdbAccessTokenAndAccountId(
            accessToken = TmdbAuthTestData.AccessToken,
            accountId = TmdbAuthTestData.AccountId
        ).right()
        coEvery { convertV4Session(TmdbAuthTestData.AccessToken, TmdbAuthTestData.AccountId) } returns
            TmdbAuthTestData.Credentials.right()
        every { getTokenAuthorizationUrl(TmdbRequestToken(any())) } returns TmdbAuthTestData.TokenAuthorizationUrl
    }
    private val repository = RealTmdbAuthRepository(
        dispatcher = dispatcher,
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

    @Test
    fun `user authorized the request token`() = runTest {
        // given
        val expected = LinkToTmdb.State.Success.right()

        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTmdb.State.UserShouldAuthorizeToken>(authorizeItem)

            // when
            repository.notifyTokenAuthorized()

            // then
            assertEquals(expected, awaitItem())
            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                    storeAuthState(TmdbAuthState.RequestTokenCreated(TmdbAuthTestData.RequestToken))
                    findAuthState()
                    storeAuthState(TmdbAuthState.RequestTokenAuthorized(TmdbAuthTestData.AuthorizedRequestToken))
                    storeAuthState(TmdbAuthState.AccessTokenCreated(TmdbAuthTestData.AccessTokenAndAccountId))
                    storeAuthState(TmdbAuthState.Completed(TmdbAuthTestData.Credentials))
                }
            }
        }
    }

    @Test
    fun `user did not authorize the request token`() = runTest {
        // given
        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTmdb.State.UserShouldAuthorizeToken>(authorizeItem)

            // when
            // nothing

            // then
            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                    storeAuthState(TmdbAuthState.RequestTokenCreated(TmdbAuthTestData.RequestToken))
                }
            }
        }
    }

    @Test
    fun `network error happened on request token`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        coEvery { remoteDataSource.createRequestToken() } returns networkError.left()
        val expected = LinkToTmdb.Error.Network(networkError).left()

        // when
        repository.link().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `network error happened on access token`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        coEvery { remoteDataSource.createAccessToken(TmdbAuthTestData.AuthorizedRequestToken) } returns
            networkError.left()
        val expected = LinkToTmdb.Error.Network(networkError).left()

        // when
        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTmdb.State.UserShouldAuthorizeToken>(authorizeItem)

            // when
            repository.notifyTokenAuthorized()

            // then
            assertEquals(expected, awaitItem())

            coVerifySequence {
                with(localDataSource) {
                    findAuthState()
                    storeAuthState(TmdbAuthState.RequestTokenCreated(TmdbAuthTestData.RequestToken))
                    findAuthState()
                    storeAuthState(TmdbAuthState.RequestTokenAuthorized(TmdbAuthTestData.AuthorizedRequestToken))
                }
            }
        }
    }
}
