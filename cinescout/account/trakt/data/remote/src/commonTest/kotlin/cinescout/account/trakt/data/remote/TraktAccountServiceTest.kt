package cinescout.account.trakt.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.account.trakt.data.remote.testdata.GetAccountResponseTestData
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.error.NetworkError
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktAuthProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import kotlin.test.Test

class TraktAccountServiceTest {

    private val authProvider: TraktAuthProvider = mockk {
        every { refreshToken() } returns ""
    }
    private val client = CineScoutTraktClient(
        authProvider = authProvider,
        engine = MockTraktAccountEngine()
    )
    private val service = TraktAccountService(client)

    @Test
    fun `get account when logged in`() = runTest {
        // given
        val expected = GetAccountResponseTestData.Account.right()
        every { authProvider.accessToken() } returns "accessToken"

        // when
        val result = service.getAccount()

        // then
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `get account when not logged in`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        every { authProvider.accessToken() } returns null

        // when
        val result = service.getAccount()

        // then
        Assert.assertEquals(expected, result)
    }
}
