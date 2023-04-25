package cinescout.auth.trakt.data.remote.service

import arrow.core.right
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.data.remote.TraktRedirectUrlString
import cinescout.auth.trakt.data.remote.testdata.RemoteTraktAuthTestData
import cinescout.auth.trakt.data.remote.testutil.TraktAuthMockEngine
import cinescout.network.CineScoutClient
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class TraktAuthServiceTest : AnnotationSpec() {

    private val client = CineScoutClient(TraktAuthMockEngine())
    private val service = TraktAuthService(
        client = client,
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUrl = TraktRedirectUrlString
    )

    @Test
    fun `creates access token from code`() = runTest {
        // given
        val expected = RemoteTraktAuthTestData.CreateAccessTokenResponse.right()

        // when
        val result = service.createAccessToken(TraktAuthorizationCodeSample.AuthorizationCode)

        // then
        result shouldBe expected
    }
}
