package cinescout.auth.trakt.data.remote

import arrow.core.right
import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.auth.trakt.data.remote.testdata.RemoteTraktAuthTestData
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class RealTraktAuthRemoteDataSourceTest : AnnotationSpec() {

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
        result shouldBe expected
    }
}
