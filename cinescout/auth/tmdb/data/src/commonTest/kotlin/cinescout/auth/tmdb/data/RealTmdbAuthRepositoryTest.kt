package cinescout.auth.tmdb.data

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealTmdbAuthRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val remoteDataSource: TmdbAuthRemoteDataSource = mockk {
        coEvery { createRequestToken() } returns RequestToken.right()
        coEvery { createAccessToken(Authorized(RequestToken)) } returns AccessToken.right()
    }
    private val repository = RealTmdbAuthRepository(dispatcher = dispatcher, remoteDataSource = remoteDataSource)

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
        }
    }

    @Test
    fun `network error happened on access token`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        coEvery { remoteDataSource.createAccessToken(Authorized(RequestToken)) } returns networkError.left()
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
        }
    }

    companion object TestData {

        private val AccessToken = TmdbAccessToken("Access Token")
        private val RequestToken = TmdbRequestToken("Request Token")
    }
}
