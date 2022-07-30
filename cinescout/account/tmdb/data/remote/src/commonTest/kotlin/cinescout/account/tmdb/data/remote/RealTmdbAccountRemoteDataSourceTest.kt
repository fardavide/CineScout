package cinescout.account.tmdb.data.remote

import arrow.core.right
import cinescout.account.tmdb.data.remote.testdata.GetAccountResponseTestData
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.Test

class RealTmdbAccountRemoteDataSourceTest {

    private val service: TmdbAccountService = mockk {
        coEvery { getAccount() } returns GetAccountResponseTestData.Account.right()
    }
    private val dataSource = RealTmdbAccountRemoteDataSource(service)

    @Test
    fun `get account from service`() = runTest {
        // given
        val expected = TmdbAccountTestData.Account.right()

        // when
        val result = dataSource.getAccount()

        // then
        assertEquals(expected, result)
    }
}
