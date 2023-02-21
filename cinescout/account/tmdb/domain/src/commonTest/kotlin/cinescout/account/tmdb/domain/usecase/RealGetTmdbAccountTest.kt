package cinescout.account.tmdb.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.tmdb.domain.TmdbAccountRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealGetTmdbAccountTest {

    private val accountRepository: TmdbAccountRepository = mockk()
    private val getTmdbAccount = RealGetTmdbAccount(accountRepository)

    @Test
    fun `get account from repository`() = runTest {
        // given
        val expected = AccountSample.Tmdb.right()
        every { accountRepository.getAccount(refresh = any()) } returns flowOf(expected)

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
        every { accountRepository.getAccount(refresh = any()) } returns flowOf(expected)

        // when
        getTmdbAccount().test {

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
