package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.*

class RealTmdbAuthProviderTest {

    private val dataSource: TmdbAuthLocalDataSource = mockk {
        every { findCredentialsBlocking() } returns TmdbCredentials(
            accessToken = TmdbAuthTestData.AccessToken,
            accountId = TmdbAuthTestData.AccountId,
            sessionId = TmdbAuthTestData.SessionId
        )
    }
    private val provider = RealTmdbAuthProvider(dataSource)

    @Test
    fun `get access token from data source`() {
        // given
        val expected = TmdbAuthTestData.AccessToken.value

        // when
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        verify { dataSource.findCredentialsBlocking() }
    }

    @Test
    fun `get access token from cached value`() {
        // given
        val expected = TmdbAuthTestData.AccessToken.value

        // when
        provider.accessToken()
        val result = provider.accessToken()

        // then
        assertEquals(expected, result)
        verify(exactly = 1) { dataSource.findCredentialsBlocking() }
    }

    @Test
    fun `get session id from data source`() {
        // given
        val expected = TmdbAuthTestData.SessionId.value

        // when
        val result = provider.sessionId()

        // then
        assertEquals(expected, result)
        verify { dataSource.findCredentialsBlocking() }
    }

    @Test
    fun `get session id from cached value`() {
        // given
        val expected = TmdbAuthTestData.SessionId.value

        // when
        provider.sessionId()
        val result = provider.sessionId()

        // then
        assertEquals(expected, result)
        verify(exactly = 1) { dataSource.findCredentialsBlocking() }
    }
}
