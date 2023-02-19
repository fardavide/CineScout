package cinescout.auth.tmdb.data.remote.service

import arrow.core.right
import cinescout.auth.tmdb.data.remote.TmdbRedirectUrl
import cinescout.auth.tmdb.data.remote.testdata.RemoteTmdbAuthTestData
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenSample
import cinescout.network.CineScoutClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbAuthServiceTest {

    private val client = CineScoutClient(MockTmdbAuthEngine())
    private val service = TmdbAuthService(redirectUrl = TmdbRedirectUrl, v3client = client, v4client = client)

    @Test
    fun `creates request token`() = runTest {
        // given
        val expected = RemoteTmdbAuthTestData.CreateRequestTokenResponse.right()

        // when
        val result = service.createRequestToken()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `creates access token`() = runTest {
        // given
        val expected = RemoteTmdbAuthTestData.CreateAccessTokenResponse.right()

        // when
        val result = service.createAccessToken(RemoteTmdbAuthTestData.AuthorizedRequestToken)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `converts v4 session`() = runTest {
        // given
        val expected = RemoteTmdbAuthTestData.ConvertV4SessionResponse.right()

        // when
        val result = service.convertV4Session(TmdbAccessTokenSample.AccessToken)

        // then
        assertEquals(expected, result)
    }
}
