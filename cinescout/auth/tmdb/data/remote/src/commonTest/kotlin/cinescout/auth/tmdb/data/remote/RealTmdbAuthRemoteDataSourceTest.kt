package cinescout.auth.tmdb.data.remote

import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.remote.model.ConvertV4Session
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenSample
import cinescout.auth.tmdb.data.sample.TmdbAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbAuthorizedRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbCredentialsSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbSessionIdSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAuthRemoteDataSourceTest {

    private val authService: TmdbAuthService = mockk {
        coEvery { createRequestToken() } returns CreateRequestToken.Response(
            requestToken = TmdbRequestTokenSample.RequestToken.value
        ).right()
        coEvery { createAccessToken(TmdbAuthorizedRequestTokenSample.AuthorizedRequestToken) } returns
            CreateAccessToken.Response(
                accessToken = TmdbAccessTokenSample.AccessToken.value,
                accountId = TmdbAccountIdSample.AccountId.value
            ).right()
        coEvery { convertV4Session(TmdbAccessTokenSample.AccessToken) } returns ConvertV4Session.Response(
            sessionId = TmdbSessionIdSample.SessionId.value
        ).right()
    }
    private val dataSource = RealTmdbAuthRemoteDataSource(authService)

    @Test
    fun `creates request token`() = runTest {
        // given
        val expected = TmdbRequestTokenSample.RequestToken.right()

        // when
        val result = dataSource.createRequestToken()

        // then
        coVerify { authService.createRequestToken() }
        assertEquals(expected, result)
    }

    @Test
    fun `creates access token`() = runTest {
        // given
        val requestToken = TmdbAuthorizedRequestTokenSample.AuthorizedRequestToken
        val expected = TmdbAccessTokenAndAccountId(
            accessToken = TmdbAccessTokenSample.AccessToken,
            accountId = TmdbAccountIdSample.AccountId
        ).right()

        // when
        val result = dataSource.createAccessToken(requestToken)

        // then
        coVerify { authService.createAccessToken(requestToken) }
        assertEquals(expected, result)
    }

    @Test
    fun `converts v4 session`() = runTest {
        // given
        val accessToken = TmdbAccessTokenSample.AccessToken
        val accountId = TmdbAccountIdSample.AccountId
        val expected = TmdbCredentialsSample.Credentials.right()

        // when
        val result = dataSource.convertV4Session(accessToken, accountId)

        // then
        coVerify { authService.convertV4Session(accessToken) }
        assertEquals(expected, result)
    }
}
