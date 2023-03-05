package cinescout.auth.trakt.data.remote.service

import arrow.core.right
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.data.remote.TraktRedirectUrl
import cinescout.auth.trakt.data.remote.testdata.RemoteTraktAuthTestData
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.network.CineScoutClient
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktAuthServiceTest {

    private val client = CineScoutClient(MockTraktAuthEngine())
    private val service = TraktAuthService(
        client = client,
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUrl = TraktRedirectUrl
    )

    @Test
    fun `creates access token from code`() = runTest {
        // given
        val expected = RemoteTraktAuthTestData.CreateAccessTokenResponse.right()

        // when
        val result = service.createAccessToken(TraktAuthorizationCodeSample.AuthorizationCode)

        // then
        assertEquals(expected, result)
    }
}
