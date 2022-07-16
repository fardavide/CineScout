package cinescout.auth.tmdb.data.remote

import arrow.core.right
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.remote.model.ConvertV4Session
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAuthRemoteDataSourceTest {

    private val authService: TmdbAuthService = mockk {
        coEvery { createRequestToken() } returns CreateRequestToken.Response(
            requestToken = TmdbAuthTestData.RequestToken.value
        ).right()
        coEvery { createAccessToken(TmdbAuthTestData.AuthorizedRequestToken) } returns CreateAccessToken.Response(
            accessToken = TmdbAuthTestData.AccessToken.value,
            accountId = TmdbAuthTestData.AccountId.value
        ).right()
        coEvery { convertV4Session(TmdbAuthTestData.AccessToken) } returns ConvertV4Session.Response(
            sessionId = TmdbAuthTestData.SessionId.value
        ).right()
    }
    private val dataSource = RealTmdbAuthRemoteDataSource(authService)

    @Test
    fun `creates request token`() = runTest {
        // given
        val expected = TmdbAuthTestData.RequestToken.right()

        // when
        val result = dataSource.createRequestToken()

        // then
        coVerify { authService.createRequestToken() }
        assertEquals(expected, result)
    }

    @Test
    fun `creates access token`() = runTest {
        // given
        val requestToken = TmdbAuthTestData.AuthorizedRequestToken
        val expected = TmdbAccessTokenAndAccountId(
            accessToken = TmdbAuthTestData.AccessToken,
            accountId = TmdbAuthTestData.AccountId
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
        val accessToken = TmdbAuthTestData.AccessToken
        val accountId = TmdbAuthTestData.AccountId
        val expected = TmdbAuthTestData.Credentials.right()

        // when
        val result = dataSource.convertV4Session(accessToken, accountId)

        // then
        coVerify { authService.convertV4Session(accessToken) }
        assertEquals(expected, result)
    }
}
