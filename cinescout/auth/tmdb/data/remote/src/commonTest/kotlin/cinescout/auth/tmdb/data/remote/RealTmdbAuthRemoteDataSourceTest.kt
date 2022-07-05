package cinescout.auth.tmdb.data.remote

import arrow.core.right
import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.Test

class RealTmdbAuthRemoteDataSourceTest {

    private val authService: TmdbAuthService = mockk {
        coEvery { createRequestToken() } returns CreateRequestToken.Response(requestToken = RequestToken).right()
        coEvery { createAccessToken(Authorized(RequestToken)) } returns CreateAccessToken.Response(
            accessToken = AccessToken,
            accountId = "account"
        ).right()
    }
    private val dataSource = RealTmdbAuthRemoteDataSource(authService)

    @Test
    fun `creates request token`() = runTest {
        // given
        val expected = TmdbRequestToken(RequestToken).right()

        // when
        val result = dataSource.createRequestToken()

        // then
        coVerify { authService.createRequestToken() }
        assertEquals(expected, result)
    }

    @Test
    fun `creates access token`() = runTest {
        // given
        val requestToken = Authorized(RequestToken)
        val expected = TmdbAccessToken(AccessToken).right()

        // when
        val result = dataSource.createAccessToken(requestToken)

        // then
        coVerify { authService.createAccessToken(requestToken) }
        assertEquals(expected, result)
    }

    companion object TestData {

        private const val AccessToken = "Access Token"
        private const val RequestToken = "Request Token"
    }
}
