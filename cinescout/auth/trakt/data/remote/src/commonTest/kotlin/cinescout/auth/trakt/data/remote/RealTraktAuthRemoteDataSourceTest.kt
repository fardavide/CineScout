package cinescout.auth.trakt.data.remote

import arrow.core.right
import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.auth.trakt.data.remote.testdata.RemoteTraktAuthTestData
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthRemoteDataSourceTest {

    private val authService: TraktAuthService = mockk {
        coEvery { createAccessToken(code = TraktAuthorizationCode(any())) } returns
            RemoteTraktAuthTestData.CreateAccessTokenResponse.right()
    }
    private val dataSource = RealTraktAuthRemoteDataSource(authService)

    @Test
    fun `create access token from code`() = runTest {
        // given
        val expected = TraktAccessAndRefreshTokensSample.Tokens.right()

        // when
        val result = dataSource.createAccessToken(TraktAuthorizationCodeSample.AuthorizationCode)

        // then
        coVerify { authService.createAccessToken(TraktAuthorizationCodeSample.AuthorizationCode) }
        assertEquals(expected, result)
    }
}
