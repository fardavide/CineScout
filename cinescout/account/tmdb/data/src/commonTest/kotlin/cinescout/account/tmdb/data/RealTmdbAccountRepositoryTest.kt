package cinescout.account.tmdb.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.tmdb.domain.model.GetAccountError
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAccountRepositoryTest {

    private val dataSource: TmdbAccountRemoteDataSource = mockk()
    private val repository = RealTmdbAccountRepository(dataSource)

    @Test
    fun `account from repository`() = runTest {
        // given
        val expected = TmdbAccountTestData.Account.right()
        coEvery { dataSource.getAccount() } returns expected

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `error from repository`() = runTest {
        // given
        val error = NetworkError.NoNetwork
        val expected = GetAccountError.Network(error).left()
        coEvery { dataSource.getAccount() } returns error.left()

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `no account connected if unauthorized from repository`() = runTest {
        // given
        val error = NetworkError.Unauthorized
        val expected = GetAccountError.NoAccountConnected.left()
        coEvery { dataSource.getAccount() } returns error.left()

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
