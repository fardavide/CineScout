package cinescout.account.tmdb.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.GetAccountError
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.error.NetworkError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import store.test.MockStoreOwner
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAccountRepositoryTest {

    private val dispatcher = StandardTestDispatcher()
    private val localDataSource: TmdbAccountLocalDataSource = mockk(relaxUnitFun = true) {
        every { findAccount() } returns flowOf(null)
    }
    private val remoteDataSource: TmdbAccountRemoteDataSource = mockk()
    private val storeOwner = MockStoreOwner(dispatcher)
    private val repository = RealTmdbAccountRepository(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        storeOwner = storeOwner
    )

    @Test
    fun `account from local source`() = runTest(dispatcher) {
        // given
        val account = TmdbAccountTestData.Account
        val expected = account.right()
        storeOwner.updated()
        coEvery { remoteDataSource.getAccount() } returns NetworkError.NoNetwork.left()
        coEvery { localDataSource.findAccount() } returns flowOf(account)

        // when
        repository.getAccount().test {

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `account from remote source`() = runTest(dispatcher) {
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
    fun `error from remote source`() = runTest(dispatcher) {
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
    fun `no account connected if unauthorized from remote source`() = runTest(dispatcher) {
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
