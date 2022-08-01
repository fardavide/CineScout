package cinescout.account.trakt.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.trakt.domain.TraktAccountRepository
import cinescout.account.trakt.domain.testData.TraktAccountTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTraktAccountTest {

    private val accountRepository: TraktAccountRepository = mockk()
    private val getTmdbAccount = GetTraktAccount(accountRepository)

    @Test
    fun `get account from repository`() = runTest {
        // given
        val expected = TraktAccountTestData.Account.right()
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

