package cinescout.auth.tmdb.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.error.NetworkError
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealTmdbAuthRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val localDataSource: TmdbAuthLocalDataSource = mockk(relaxUnitFun = true)
    private val remoteDataSource: TmdbAuthRemoteDataSource = mockk {
        coEvery { createRequestToken() } returns TmdbAuthTestData.RequestToken.right()
        coEvery { createAccessToken(TmdbAuthTestData.AuthorizedRequestToken) } returns TmdbAccessTokenAndAccountId(
            accessToken = TmdbAuthTestData.AccessToken,
            accountId = TmdbAuthTestData.AccountId
        ).right()
        coEvery { convertV4Session(TmdbAuthTestData.AccessToken, TmdbAuthTestData.AccountId) } returns
            TmdbAuthTestData.Credentials.right()
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
            launch {
                authorizeItem.authorizationResultChannel.send(LinkToTmdb.TokenAuthorized.right())
            }

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coVerify { localDataSource.storeCredentials(TmdbAuthTestData.Credentials) }
        }
    }

    @Test
    fun `user did not authorize the request token`() = runTest {
        // given
        val expected = LinkToTmdb.Error.UserDidNotAuthorizeToken.left()

        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTmdb.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTmdb.State.UserShouldAuthorizeToken>(authorizeItem)

            // when
            launch {
                authorizeItem.authorizationResultChannel.send(LinkToTmdb.TokenNotAuthorized.left())
            }

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coVerify { localDataSource wasNot Called }
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
            awaitComplete()
            coVerify { localDataSource wasNot Called }
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
            launch {
                authorizeItem.authorizationResultChannel.send(LinkToTmdb.TokenAuthorized.right())
            }

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coVerify { localDataSource wasNot Called }
        }
    }
}
