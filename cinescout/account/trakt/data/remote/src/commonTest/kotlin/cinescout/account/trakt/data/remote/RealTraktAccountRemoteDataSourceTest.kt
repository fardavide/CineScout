package cinescout.account.trakt.data.remote

import arrow.core.right
import cinescout.account.domain.sample.AccountSample
import cinescout.account.trakt.data.remote.testdata.GetAccountResponseTestData
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAccountRemoteDataSourceTest {

    private val callWithTraktAccount = CallWithTraktAccount(
        appScope = TestScope(context = UnconfinedTestDispatcher()),
        isTraktLinked = mockk {
            every { this@mockk.invoke() } returns flowOf(true)
        }
    )
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

