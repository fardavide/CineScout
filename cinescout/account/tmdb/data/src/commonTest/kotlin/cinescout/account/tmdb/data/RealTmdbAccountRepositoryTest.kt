package cinescout.account.tmdb.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.error.DataError
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAccountRepositoryTest {

    private val localDataSource: TmdbAccountLocalDataSource = mockk(relaxUnitFun = true) {
        every { findAccount() } returns flowOf(DataError.Local.NoCache.left())
    }
    private val remoteDataSource: TmdbAccountRemoteDataSource = mockk()
    private val repository = RealTmdbAccountRepository(localDataSource, remoteDataSource)

    @Test
    fun `account from local source`() = runTest {
        // given
        val expected = TmdbAccountTestData.Account.right()
        coEvery { remoteDataSource.getAccount() } returns NetworkError.NoNetwork.left()
        coEvery { localDataSource.findAccount() } returns flowOf(expected)

        // when
        repository.getAccount().test {

            // then
            awaitItem() // Remote error
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `account from remote source`() = runTest {
        // given
        val expected = TmdbAccountTestData.Account.right()
        coEvery { remoteDataSource.getAccount() } returns expected

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `error from remote source`() = runTest {
        // given
        val error = NetworkError.NoNetwork
        val expected = GetAccountError.Network(error).left()
        coEvery { remoteDataSource.getAccount() } returns error.left()

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `no account connected if unauthorized from remote source`() = runTest {
        // given
        val error = NetworkError.Unauthorized
        val expected = GetAccountError.NoAccountConnected.left()
        coEvery { remoteDataSource.getAccount() } returns error.left()

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
