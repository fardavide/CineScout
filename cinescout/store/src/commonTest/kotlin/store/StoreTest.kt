package store

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.test.kotlin.TestTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import store.test.MockStoreOwner
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StoreTest {

    private val owner = MockStoreOwner()

    @Test
    fun `first returns local data only if available`() = runTest {
        // given
        val localData = 0
        val expected = localData.right()
        val error = NetworkOperation.Error(NetworkError.NoNetwork)
        val store = owner.updated().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                error.left()
            },
            write = {},
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
            assertEquals(DataError.Remote(error.error).left(), awaitItem())
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `first returns remote data if local data is not available`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        val localData = 0
        val expected = DataError.Remote(networkError = networkError).left()
        val store = owner.fresh().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                NetworkOperation.Error(networkError).left()
            },
            write = {},
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `does emit local data if updated and network call is skipped`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 0
        val store = owner.updated().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                NetworkOperation.Skipped.left()
            },
            write = {},
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            assertEquals(emptyList(), cancelAndConsumeRemainingEvents())
        }
    }

    @Test
    fun `does emit local data if data if not updated and network call is skipped`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 0
        val store = owner.fresh().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                NetworkOperation.Skipped.left()
            },
            write = {},
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `does emit local error if local data not available and network call is skipped`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val store = owner.fresh().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                NetworkOperation.Skipped.left()
            },
            write = {},
            read = { flowOf(null) }
        )

        // when
        store.test {

            // then
            assertEquals(DataError.Local.NoCache.left(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `returns local data then local data refreshed from remote`() = runTest {
        // given
        val localData = 1
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = owner.updated().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `returns local data then local data refreshed from remote, after error`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val networkError = NetworkError.NoNetwork
        val dataFromAnotherSource = 2
        val expectedError = DataError.Remote(networkError = networkError).left()

        val localFlow: MutableStateFlow<Int> =
            MutableStateFlow(localData)

        val store = owner.updated().Store(
            key = TestKey,
            fetch = {
                delay(NetworkDelay)
                NetworkOperation.Error(networkError).left()
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            assertEquals(expectedError, awaitItem())
            localFlow.emit(dataFromAnotherSource)
            assertEquals(localData.right(), awaitItem())
            assertEquals(dataFromAnotherSource.right(), awaitItem())
        }
    }

    @Test
    fun `refresh when refresh is interval and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val firstRemoteData = 2.right()
        val secondRemoteData = 3.right()
        var remoteDataCount = 1

        val localFlow = MutableStateFlow(localData)

        val store = owner.updated().Store(
            key = TestKey,
            refresh = Refresh.WithInterval(),
            fetch = {
                delay(NetworkDelay)
                remoteDataCount++.right()
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            assertEquals(firstRemoteData, awaitItem())
            assertEquals(secondRemoteData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh when refresh is once and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = owner.updated().Store(
            key = TestKey,
            refresh = Refresh.Once,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `refresh when refresh is once and local data is not available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData: Int? = null
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = owner.fresh().Store(
            key = TestKey,
            refresh = Refresh.Once,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `do not refresh when refresh is if needed and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val remoteData = 2.right()

        val store = owner.updated().Store(
            key = TestKey,
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
            assertEquals(localData.right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `refresh when refresh is if needed and local data is not available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData: Int? = null
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = owner.fresh().Store(
            key = TestKey,
            refresh = Refresh.IfNeeded,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `refresh when refresh is if expired and local data is available, but expired`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val remoteData = 2.right()

        val store = owner.expired().Store(
            key = TestKey,
            refresh = Refresh.IfExpired(),
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
            assertEquals(localData.right(), awaitItem())
            assertEquals(remoteData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `do not refresh when refresh is if expired and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val remoteData = 2.right()

        val store = owner.updated().Store(
            key = TestKey,
            refresh = Refresh.IfExpired(),
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
            assertEquals(localData.right(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `refresh when refresh is if expired and local data is not available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val remoteData = 2.right()

        val localFlow = MutableStateFlow<Int?>(null)

        val store = owner.fresh().Store(
            key = TestKey,
            refresh = Refresh.IfExpired(),
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(remoteData, awaitItem())
        }
    }

    @Test
    fun `do not refresh when refresh is never and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = 1
        val remoteData = 2.right()

        val localFlow = MutableStateFlow(localData)

        val store = owner.updated().Store(
            key = TestKey,
            refresh = Refresh.Never,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.right(), awaitItem())
        }
    }

    @Test
    fun `do not refresh when refresh is never and local data is not available`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData: Int? = null
        val remoteData = 2.right()
        val expected = DataError.Local.NoCache.left()

        val localFlow = MutableStateFlow(localData)

        val store = owner.fresh().Store(
            key = TestKey,
            refresh = Refresh.Never,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private companion object {

        const val NetworkDelay = 100L
        val TestKey = StoreKey("test")
    }
}
