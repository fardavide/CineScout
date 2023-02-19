package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenSample
import cinescout.auth.tmdb.data.sample.TmdbAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbCredentialsSample
import cinescout.auth.tmdb.data.sample.TmdbSessionIdSample
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAuthProviderTest {

    private val dataSource: TmdbAuthLocalDataSource = mockk {
        coEvery { findCredentials() } returns TmdbCredentialsSample.Credentials
    }
    private val provider = RealTmdbAuthProvider(dataSource)

    @Test
    fun `get access token from data source`() = runTest {
        // given
        val expected = TmdbAccessTokenSample.AccessToken.value

        // when
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        coVerify { dataSource.findCredentials() }
    }

    @Test
    fun `get access token from cached value`() = runTest {
        // given
        val expected = TmdbAccessTokenSample.AccessToken.value

        // when
        provider.accessToken()
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        coVerify(exactly = 1) { dataSource.findCredentials() }
    }

    @Test
    fun `get account id from data source`() = runTest {
        // given
        val expected = TmdbAccountIdSample.AccountId.value

        // when
        val result = provider.accountId()

        // then
        assertEquals(expected, result)
        coVerify { dataSource.findCredentials() }
    }

    @Test
    fun `get account id from cached value`() = runTest {
        // given
        val expected = TmdbAccountIdSample.AccountId.value

        // when
        provider.accountId()
        val result = provider.accountId()

        // then
        assertEquals(expected, result)
        coVerify(exactly = 1) { dataSource.findCredentials() }
    }

    @Test
    fun `get session id from data source`() = runTest {
        // given
        val expected = TmdbSessionIdSample.SessionId.value

        // when
        val result = provider.sessionId()

        // then
        assertEquals(expected, result)
        coVerify { dataSource.findCredentials() }
    }

    @Test
    fun `get session id from cached value`() = runTest {
        // given
        val expected = TmdbSessionIdSample.SessionId.value

        // when
        provider.sessionId()
        val result = provider.sessionId()

        // then
        assertEquals(expected, result)
        coVerify(exactly = 1) { dataSource.findCredentials() }
    }
}
