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
import kotlinx.coroutines.test.runTest
import store.builder.toPagedData
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PagedStoreTest {

    @Test
    fun `paged store returns local data then local data refreshed from remote, after error`() = runTest {
        // given
        val networkError = NetworkError.NoNetwork
        val dataFromAnotherSource = listOf(2).right()
        val pagedDataFromAnotherSource = listOf(2).toPagedData().right()
        val expectedError = DataError.Remote(networkError = networkError).left()

        val localFlow: MutableStateFlow<Either<DataError.Local, List<Int>>> =
            MutableStateFlow(DataError.Local.NoCache.left())

        val store: PagedStore<Int, Paging> = PagedStore(
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
            assertEquals(pagedDataFromAnotherSource, awaitItem())
        }
    }

    @Test
    fun `paged store filter all intermediate pages`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val totalPages = 3
        val expected = buildLocalData(totalPages, totalPages = totalPages)
        val localData = listOf(1)

        val localFlow = MutableStateFlow(localData.right())

        val flow = PagedStore<Int, Int, Paging.Page.SingleSource, Paging>(
            initialBookmark = 1,
            createNextBookmark = { _, currentPage -> currentPage + 1 },
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page, totalPages = totalPages)
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        ).filterIntermediatePages()

        // when
        flow.test {

            // then
            assertEquals(expected, awaitItem())
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

        fun loadRemoteData(
            page: Int,
            totalPages: Int = 5
        ): Either<Nothing, PagedData.Remote<Int, Paging.Page.SingleSource>> {
            if (page > totalPages) throw IllegalStateException("Page $page is too high")
            return PagedData.Remote(
                data = listOf(page),
                paging = Paging.Page(page = page, totalPages = totalPages)
            ).right()
        }

        fun buildLocalData(page: Int, totalPages: Int = 5) = PagedData.Remote(
            data = (1..page).toList(),
            paging = Paging.Page(
                page = page,
                totalPages = totalPages
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
