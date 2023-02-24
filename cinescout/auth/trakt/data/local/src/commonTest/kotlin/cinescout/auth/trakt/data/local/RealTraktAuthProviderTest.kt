package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.data.sample.TraktAccessTokenSample
import cinescout.auth.trakt.data.sample.TraktRefreshTokenSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthProviderTest {

    private val dataSource: TraktAuthLocalDataSource = mockk {
        coEvery { findTokens() } returns TraktAccessAndRefreshTokensSample.Tokens
    }
    private val provider = RealTraktAuthProvider(dataSource)

    @Test
    fun `get access token from data source`() = runTest {
        // given
        val expected = TraktAccessTokenSample.AccessToken.value

        // when
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        coVerify { dataSource.findTokens() }
    }

    @Test
    fun `get access token from cached value`() = runTest {
        // given
        val expected = TraktAccessTokenSample.AccessToken.value

        // when
        provider.accessToken()
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        coVerify(exactly = 1) { dataSource.findTokens() }
    }

    @Test
    fun `get refresh token from data source`() = runTest {
        // given
        val expected = TraktRefreshTokenSample.RefreshToken.value

        // when
        val result = provider.refreshToken()

        // then
        assertEquals(expected, result)
        coVerify { dataSource.findTokens() }
    }

    @Test
    fun `get refresh token from cached value`() = runTest {
        // given
        val expected = TraktRefreshTokenSample.RefreshToken.value

        // when
        provider.refreshToken()
        val result = provider.refreshToken()

        // then
        assertEquals(expected, result)
        coVerify(exactly = 1) { dataSource.findTokens() }
    }
}
