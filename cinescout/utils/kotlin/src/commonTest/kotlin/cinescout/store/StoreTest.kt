package cinescout.store

import app.cash.turbine.test
import arrow.core.Either
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
            cancelAndIgnoreRemainingEvents()
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
            cancelAndIgnoreRemainingEvents()
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
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `paged store loads more`() = runTest {
        // given
        val localData = listOf(1)
        val localPagedData = localData.toPagedData().right()

        val localFlow = MutableStateFlow(localData.right())

        val store: PagedStore<Int, Paging> = PagedStore(
            initialBookmark = 2,
            createNextBookmark = { _, currentPage -> currentPage + 1 },
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
        val localPagedData = localData.toPagedData().right()

        val localFlow = MutableStateFlow(localData.right())

        val store: PagedStore<Int, Paging> = PagedStore(
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
            assertEquals(localPagedData, awaitItem())
            assertEquals(buildLocalData(1), awaitItem())
            store.loadAll()
            assertEquals(buildLocalData(2), awaitItem())
            assertEquals(buildLocalData(3), awaitItem())
            assertEquals(buildLocalData(4), awaitItem())
            assertEquals(buildLocalData(5), awaitItem())
        }
    }

    @Test
    fun `paged store get all`() = runTest {
        // given
        val localData = listOf(1)
        val expected = buildLocalData(5).map { it.data }

        val localFlow = MutableStateFlow(localData.right())

        val store: PagedStore<Int, Paging> = PagedStore(
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page)
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        assertEquals(expected, store.getAll())
    }

    private companion object {

        const val NetworkDelay = 100L

        fun loadRemoteData(page: Int): Either<Nothing, PagedData.Remote<Int, Paging.Page.SingleSource>> {
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
                totalPages = 5
            )
        ).right()

        private suspend fun <L, R> MutableStateFlow<Either<L, List<R>>>.add(others: List<R>) {
            value.fold(
                ifLeft = { emit(others.right()) },
                ifRight = { emit((it + others).distinct().right()) }
            )
        }
    }
}
