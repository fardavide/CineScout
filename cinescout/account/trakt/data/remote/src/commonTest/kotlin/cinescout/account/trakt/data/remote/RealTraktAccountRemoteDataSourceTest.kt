package cinescout.account.trakt.data.remote

import arrow.core.right
import cinescout.account.trakt.data.remote.testdata.GetAccountResponseTestData
import cinescout.account.trakt.domain.testData.TraktAccountTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAccountRemoteDataSourceTest {

    private val service: TraktAccountService = mockk {
        coEvery { getAccount() } returns GetAccountResponseTestData.Account.right()
    }
    private val dataSource = RealTraktAccountRemoteDataSource(service)

    @Test
    fun `get account from service`() = runTest {
        // given
        val expected = TraktAccountTestData.Account.right()

        // when
        val result = dataSource.getAccount()

        // then
        assertEquals(expected, result)
    }
}

