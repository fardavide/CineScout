package cinescout.auth.trakt.data.local

import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAuthProviderTest {

    private val dataSource: TraktAuthLocalDataSource = mockk {
        every { findTokensBlocking() } returns TraktAuthTestData.AccessAndRefreshToken
    }
    private val provider = RealTraktAuthProvider(dataSource)

    @Test
    fun `get access token from data source`() {
        // given
        val expected = TraktAuthTestData.AccessToken.value

        // when
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        verify { dataSource.findTokensBlocking() }
    }

    @Test
    fun `get access token from cached value`() {
        // given
        val expected = TraktAuthTestData.AccessToken.value

        // when
        provider.accessToken()
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        verify(exactly = 1) { dataSource.findTokensBlocking() }
    }

    @Test
    fun `get refresh token from data source`() {
        // given
        val expected = TraktAuthTestData.RefreshToken.value

        // when
        val result = provider.refreshToken()

        // then
        assertEquals(expected, result)
        verify { dataSource.findTokensBlocking() }
    }

    @Test
    fun `get refresh token from cached value`() {
        // given
        val expected = TraktAuthTestData.RefreshToken.value

        // when
        provider.refreshToken()
        val result = provider.refreshToken()

        // then
        assertEquals(expected, result)
        verify(exactly = 1) { dataSource.findTokensBlocking() }
    }
}
