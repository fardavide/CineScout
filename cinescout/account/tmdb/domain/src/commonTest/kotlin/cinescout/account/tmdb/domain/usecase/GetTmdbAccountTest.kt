package cinescout.account.tmdb.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.account.tmdb.domain.model.GetAccountError
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTmdbAccountTest {

    private val accountRepository: TmdbAccountRepository = mockk()
    private val getTmdbAccount = GetTmdbAccount(accountRepository)

    @Test
    fun `get account from repository`() = runTest {
        // given
        val expected = TmdbAccountTestData.Account.right()
        every { accountRepository.getAccount() } returns flowOf(expected)

        // when
        getTmdbAccount().test {

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get error from repository`() = runTest {
        // given
        val expected = GetAccountError.NoAccountConnected.left()
        every { accountRepository.getAccount() } returns flowOf(expected)

        // when
        getTmdbAccount().test {

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
