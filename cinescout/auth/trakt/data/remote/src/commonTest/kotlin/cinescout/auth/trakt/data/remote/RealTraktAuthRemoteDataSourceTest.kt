package cinescout.auth.trakt.data.remote

import arrow.core.right
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.auth.trakt.data.remote.testdata.RemoteTraktAuthTestData
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthRemoteDataSourceTest {

    private val authService: TraktAuthService = mockk {
        coEvery { createAccessToken(code = TraktAuthorizationCode(any())) } returns RemoteTraktAuthTestData.CreateAccessTokenResponse.right()
        coEvery { createAccessToken(refreshToken = TraktRefreshToken(any())) } returns RemoteTraktAuthTestData.CreateAccessTokenResponse.right()
    }
    private val dataSource = RealTraktAuthRemoteDataSource(authService)

    @Test
    fun `create access token from code`() = runTest {
        // given
        val expected = TraktAuthTestData.AccessAndRefreshToken.right()

        // when
        val result = dataSource.createAccessToken(TraktAuthTestData.AuthorizationCode)

        // then
        coVerify { authService.createAccessToken(TraktAuthTestData.AuthorizationCode) }
        assertEquals(expected, result)
    }

    @Test
    fun `create access token from refresh token`() = runTest {
        // given
        val expected = TraktAuthTestData.AccessAndRefreshToken.right()

        // when
        val result = dataSource.createAccessToken(TraktAuthTestData.RefreshToken)

        // then
        coVerify { authService.createAccessToken(TraktAuthTestData.RefreshToken) }
        assertEquals(expected, result)
    }
}
