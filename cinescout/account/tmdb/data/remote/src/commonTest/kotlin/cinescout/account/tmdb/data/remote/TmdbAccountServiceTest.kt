package cinescout.account.tmdb.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.account.tmdb.data.remote.testdata.GetAccountResponseTestData
import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.error.NetworkError
import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.TmdbAuthProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.Test

class TmdbAccountServiceTest {

    private val authProvider: TmdbAuthProvider = mockk()
    private val client = CineScoutTmdbV3Client(MockTmdbAccountEngine())
    private val service = TmdbAccountService(authProvider = authProvider, client = client)

    @Test
    fun `get account when logged in`() = runTest {
        // given
        val expected = GetAccountResponseTestData.Account.right()
        every { authProvider.sessionId() } returns "sessionId"

        // when
        val result = service.getAccount()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get account when not logged in`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        every { authProvider.sessionId() } returns null

        // when
        val result = service.getAccount()

        // then
        assertEquals(expected, result)
    }
}
