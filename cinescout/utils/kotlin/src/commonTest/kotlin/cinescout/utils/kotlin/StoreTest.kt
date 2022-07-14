package cinescout.utils.kotlin

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.model.PagedData
import cinescout.model.Paging
import cinescout.model.toPagedData
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
        val localData = listOf(1)
        val localPagedData = localData.toPagedData().right()
        fun loadRemoteData(page: Int) = PagedData.Remote(
            data = listOf(page),
            paging = Paging.Page(
                page = page,
                totalPages = 10,
            )
        ).right()
        fun buildLocalData(page: Int) = PagedData.Remote(
            data = (1..page).toList(),
            paging = Paging.Page(
                page = page,
                totalPages = 10,
            )
        ).right()

        val localFlow = MutableStateFlow(localData.right())

        val store = PagedStore(
            initialBookmark = 2,
            createNextBookmark = { _, currentBookmark -> currentBookmark + 1 },
            fetch = { bookmark ->
                delay(NetworkDelay)
                loadRemoteData(bookmark)
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localPagedData, awaitItem())
            assertEquals(buildLocalData(2), awaitItem())
            store.loadMore()
            assertEquals(buildLocalData(3), awaitItem())
        }
    }

    @Test
    fun `paged store loads all`() = runTest {
        // given
        val localData = listOf(1)
        fun loadRemoteData(page: Int): Either<Nothing, PagedData.Remote<Int>> {
            if (page > 5) throw IllegalStateException("Page $page is too high")
            return PagedData.Remote(
                data = listOf(page),
                paging = Paging.Page(page = page, totalPages = 5)
            ).right()
        }
        fun buildLocalData(page: Int) = PagedData.Remote(
            data = (1..page).toList(),
            paging = Paging.Page(
                page = page,
                totalPages = 5,
            )
        ).right()

        val localFlow = MutableStateFlow(localData.right())

        val store = PagedStore(
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page)
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(listOf(1).toPagedData().right(), awaitItem())
            assertEquals(buildLocalData(1), awaitItem())
            store.loadAll()
            assertEquals(buildLocalData(2), awaitItem())
            assertEquals(buildLocalData(3), awaitItem())
            assertEquals(buildLocalData(4), awaitItem())
            assertEquals(buildLocalData(5), awaitItem())
        }
    }

    private suspend fun <L, R> MutableStateFlow<Either<L, List<R>>>.add(others: List<R>) {
        value.fold(
            ifLeft = { emit(others.right()) },
            ifRight = { emit((it + others).distinct().right()) }
        )
    }

    companion object {

        private const val NetworkDelay = 100L
    }
}
