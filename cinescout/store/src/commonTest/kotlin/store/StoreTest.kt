package store

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.test.kotlin.TestTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StoreTest {

    @Test
    fun `first returns local data only if available`() = runTest {
        // given
        val expected = 0.right()
        val error = NetworkError.NoNetwork
        val store = Store(
            fetch = {
                delay(NetworkDelay)
                error.left()
            },
            write = {},
            read = { flowOf(expected) }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
            assertEquals(DataError.Remote(error).left(), awaitItem())
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `first returns remote data if local data is not available`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        val localData = DataError.Local.NoCache.left()
        val expected = DataError.Remote(networkError = networkError).left()
        val store = Store(
            fetch = {
                delay(NetworkDelay)
                networkError.left()
            },
            write = {},
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `returns local data then local data refreshed from remote`() = runTest {
        // given
        val localData = 1.right()
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = Store(
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData, awaitItem())
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `returns local data then local data refreshed from remote, after error`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        val dataFromAnotherSource = 2.right()
        val expectedError = DataError.Remote(networkError = networkError).left()

        val localFlow: MutableStateFlow<Either<DataError.Local, Int>> =
            MutableStateFlow(DataError.Local.NoCache.left())

        val store = Store(
            fetch = {
                delay(NetworkDelay)
                networkError.left()
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(expectedError, awaitItem())
            localFlow.emit(dataFromAnotherSource)
            assertEquals(dataFromAnotherSource, awaitItem())
        }
    }

    @Test
    fun `refresh when refresh is interval and local data is available`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = 1.right()
        val firstRemoteData = 2.right()
        val secondRemoteData = 3.right()
        var remoteDataCount = 1

        val localFlow = MutableStateFlow(localData)

        val store = Store(
            refresh = Refresh.WithInterval(),
            fetch = {
                delay(NetworkDelay)
                remoteDataCount++.right()
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData, awaitItem())
            assertEquals(firstRemoteData, awaitItem())
            assertEquals(secondRemoteData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh when refresh is once and local data is available`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = 1.right()
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = Store(
            refresh = Refresh.Once,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData, awaitItem())
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `refresh when refresh is once and local data is not available`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = DataError.Local.NoCache.left()
        val remoteData = 2.right()

        val localFlow = MutableStateFlow<Either<DataError.Local, Int>>(localData)

        val store = Store(
            refresh = Refresh.Once,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `do not refresh when refresh is if needed and local data is available`() =
        runTest(dispatchTimeoutMs = TestTimeout) {
            // given
            val localData = 1.right()
            val remoteData = 2.right()

            val store = Store(
                refresh = Refresh.IfNeeded,
                fetch = {
                    delay(NetworkDelay)
                    remoteData
                },
                write = {},
                read = { flowOf(localData) }
            )

            // when
            store.test {

                // then
                assertEquals(localData, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `refresh when refresh is if needed and local data is not available`() =
        runTest(dispatchTimeoutMs = TestTimeout) {
            // given
            val localData = DataError.Local.NoCache.left()
            val remoteData = 2.right()

            val localFlow = MutableStateFlow<Either<DataError.Local, Int>>(localData)

            val store = Store(
                refresh = Refresh.IfNeeded,
                fetch = {
                    delay(NetworkDelay)
                    remoteData
                },
                write = { localFlow.emit(it.right()) },
                read = { localFlow }
            )

            // when
            store.test {

                // then
                assertEquals(remoteData, awaitItem())
            }
        }

    @Test
    fun `do not refresh when refresh is never and local data is available`() =
        runTest(dispatchTimeoutMs = TestTimeout) {
            // given
            val localData = 1.right()
            val remoteData = 2.right()

            val localFlow = MutableStateFlow(localData)

            val store = Store(
                refresh = Refresh.Never,
                fetch = {
                    delay(NetworkDelay)
                    remoteData
                },
                write = { localFlow.emit(it.right()) },
                read = { localFlow }
            )

            // when
            store.test {

                // then
                assertEquals(localData, awaitItem())
            }
        }

    @Test
    fun `do not refresh when refresh is never and local data is not available`() =
        runTest(dispatchTimeoutMs = TestTimeout) {
            // given
            val localData = DataError.Local.NoCache.left()
            val remoteData = 2.right()

            val localFlow = MutableStateFlow<Either<DataError.Local, Int>>(localData)

            val store = Store(
                refresh = Refresh.Never,
                fetch = {
                    delay(NetworkDelay)
                    remoteData
                },
                write = { localFlow.emit(it.right()) },
                read = { localFlow }
            )

            // when
            store.test {

                // then
                assertEquals(localData, awaitItem())
            }
        }

    private companion object {

        const val NetworkDelay = 100L
    }
}
