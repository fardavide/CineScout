package cinescout.account.trakt.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.account.trakt.domain.sample.TraktAccountSample
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealGetTraktAccountTest {

    private val accountRepository: TraktAccountRepository = mockk()
    private val getTmdbAccount = RealGetTraktAccount(accountRepository)

    @Test
    fun `get account from repository`() = runTest {
        // given
        val expected = TraktAccountSample.Account.right()
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

