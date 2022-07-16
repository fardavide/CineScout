package cinescout.auth.trakt.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.error.NetworkError
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealTraktAuthRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val localDataSource: TraktAuthLocalDataSource = mockk(relaxUnitFun = true)
    private val remoteDataSource: TraktAuthRemoteDataSource = mockk {
        coEvery { createAccessToken(TraktAuthorizationCode(any())) } returns TraktAuthTestData.AccessAndRefreshToken.right()
        every { getAppAuthorizationUrl() } returns TraktAuthTestData.AppAuthorizationUrl
    }
    private val repository = RealTraktAuthRepository(
        dispatcher = dispatcher,
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

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
            launch {
                authorizeItem.authorizationResultChannel
                    .send(LinkToTrakt.AppAuthorized(TraktAuthTestData.AuthorizationCode).right())
            }

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coVerify { localDataSource.storeTokens(TraktAuthTestData.AccessAndRefreshToken) }
        }
    }

    @Test
    fun `user did not authorize the request token`() = runTest {
        // given
        val expected = LinkToTrakt.Error.UserDidNotAuthorizeApp.left()

        repository.link().test {

            val authorizeItemEither = awaitItem()
            assertIs<Either.Right<LinkToTrakt.State>>(authorizeItemEither)
            val authorizeItem = authorizeItemEither.value
            assertIs<LinkToTrakt.State.UserShouldAuthorizeApp>(authorizeItem)

            // when
            launch {
                authorizeItem.authorizationResultChannel.send(LinkToTrakt.AppNotAuthorized.left())
            }

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
            launch {
                authorizeItem.authorizationResultChannel
                    .send(LinkToTrakt.AppAuthorized(TraktAuthTestData.AuthorizationCode).right())
            }

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
            coVerify { localDataSource wasNot Called }
        }
    }
}
