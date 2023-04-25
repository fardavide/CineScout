package cinescout.account.trakt.data.remote

import arrow.core.right
import cinescout.account.domain.sample.AccountSample
import cinescout.account.trakt.data.remote.datasource.RealTraktAccountRemoteDataSource
import cinescout.account.trakt.data.remote.service.TraktAccountService
import cinescout.account.trakt.data.remote.testdata.GetAccountResponseTestData
import cinescout.auth.domain.usecase.FakeCallWithTraktAccount
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAccountRemoteDataSourceTest {

    private val callWithTraktAccount = FakeCallWithTraktAccount(isLinked = true)
    private val service: TraktAccountService = mockk {
        coEvery { getAccount() } returns GetAccountResponseTestData.Account.right()
    }
    private val dataSource = RealTraktAccountRemoteDataSource(
        callWithTraktAccount = callWithTraktAccount,
        service = service
    )

    @Test
    fun `get account from service`() = runTest {
        // given
        val expected = AccountSample.Trakt.right()

        // when
        val result = dataSource.getAccount()

        // then
        assertEquals(expected, result)
    }
}

