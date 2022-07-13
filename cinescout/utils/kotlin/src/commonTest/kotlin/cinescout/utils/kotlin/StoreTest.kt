package cinescout.utils.kotlin

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
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
        val store = Store(
            fetch = {
                delay(NetworkDelay)
                NetworkError.NoNetwork.left()
            },
            write = {},
            read = { flowOf(expected) }
        )

        // when
        store.test {

            // then
            assertEquals(expected, awaitItem())
            awaitItem()
        }
    }

    @Test
    fun `first returns remote data if local data is not available`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        val localData = DataError.Local.NoCache.left()
        val expected = DataError.Remote(networkError = networkError, localData = localData).left()
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
    fun `paged store loads more`() = runTest {
        // given
        val localData = 1.right()
        fun loadRemoteData(bookmark: Int) = bookmark.right()

        val localFlow = MutableStateFlow(localData)

        val store = PagedStore(
            initialBookmark = 2,
            createNextBookmark = { _, currentBookmark -> currentBookmark + 1 },
            fetch = { bookmark ->
                delay(NetworkDelay)
                loadRemoteData(bookmark)
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData, awaitItem())
            assertEquals(2.right(), awaitItem())
            store.loadMore()
            assertEquals(3.right(), awaitItem())
        }
    }

    @Test
    fun `paged store loads all`() = runTest {
        // given
        val localData = 1.right()
        fun loadRemoteData(bookmark: Int) = bookmark.coerceAtMost(5).right()

        val localFlow = MutableStateFlow(localData)

        val store = PagedStore(
            initialBookmark = 2,
            createNextBookmark = { _, currentBookmark -> currentBookmark + 1 },
            fetch = { bookmark ->
                delay(NetworkDelay)
                loadRemoteData(bookmark)
            },
            write = { localFlow.emit(it.right()) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData, awaitItem())
            assertEquals(2.right(), awaitItem())
            store.loadAll()
            assertEquals(3.right(), awaitItem())
            assertEquals(4.right(), awaitItem())
            assertEquals(5.right(), awaitItem())
        }
    }

    companion object {

        private const val NetworkDelay = 100L
    }
}
