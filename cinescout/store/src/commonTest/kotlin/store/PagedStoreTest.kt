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
import store.test.MockStoreOwner
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PagedStoreTest {

    private val owner = MockStoreOwner()

    @Test
    fun `emits from local data, then refresh from remote`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val localData = listOf(1)
        val page = Paging.Page.SingleSource(page = 2, totalPages = 2)
        val remoteData = loadRemoteData(page)
        val expectedUpdatedData = listOf(1, 2).toPagedData(page).right()

        val localFlow: MutableStateFlow<List<Int>> =
            MutableStateFlow(localData)

        val store = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = {
                delay(NetworkDelay)
                remoteData
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localData.toPagedData().right(), awaitItem())
            assertEquals(expectedUpdatedData, awaitItem())
        }
    }

    @Test
    fun `paged store returns local data updated from another source, after error`() = runTest(
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val networkError = NetworkError.NoNetwork
        val dataFromAnotherSource = listOf(2)
        val pagedDataFromAnotherSource = listOf(2).toPagedData().right()
        val expectedError = DataError.Remote(networkError = networkError).left()

        val localFlow: MutableStateFlow<List<Int>> =
            MutableStateFlow(emptyList())

        val store = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = {
                delay(NetworkDelay)
                networkError.left()
            },
            write = { localFlow.emit(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(expectedError, awaitItem())
            localFlow.emit(dataFromAnotherSource)
            owner.updated()
            assertEquals(pagedDataFromAnotherSource, awaitItem())
        }
    }

    @Test
    fun `paged store filter all intermediate pages`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val totalPages = 3
        val expected = buildLocalData(totalPages, totalPages = totalPages).right()
        val localData = listOf(1)

        val localFlow = MutableStateFlow(localData)

        val flow = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.page, totalPages = totalPages)
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
    fun `paged store loads more`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = listOf(1)
        val localPagedData = localData.toPagedData().right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = 5))
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localPagedData, awaitItem())
            assertEquals(buildLocalData(1).right(), awaitItem())
            store.loadMore()
            assertEquals(buildLocalData(2).right(), awaitItem())
            store.loadMore()
            assertEquals(buildLocalData(3).right(), awaitItem())
        }
    }

    @Test
    fun `paged store loads all`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = listOf(1)
        val localPagedData = localData.toPagedData().right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = 5))
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(localPagedData, awaitItem())
            assertEquals(buildLocalData(1).right(), awaitItem())
            store.loadAll()
            assertEquals(buildLocalData(2).right(), awaitItem())
            assertEquals(buildLocalData(3).right(), awaitItem())
            assertEquals(buildLocalData(4).right(), awaitItem())
            assertEquals(buildLocalData(5).right(), awaitItem())
        }
    }

    @Test
    fun `paged store get all`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val localData = listOf(1)
        val totalPages = 5
        val expected = buildLocalData(totalPages).data.right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = totalPages))
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        assertEquals(expected, store.getAll())
    }

    private companion object {

        const val NetworkDelay = 100L
        val TestKey = StoreKey<Int, String>("test")

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

        fun loadRemoteData(
            page: Paging.Page.SingleSource
        ): Either<Nothing, PagedData.Remote<Int, Paging.Page.SingleSource>> = PagedData.Remote(
            data = listOf(page.page),
            paging = page
        ).right()

        fun buildLocalData(page: Int, totalPages: Int = 5) = PagedData.Remote(
            data = (1..page).toList(),
            paging = Paging.Page(
                page = page,
                totalPages = totalPages
            )
        )

        private suspend fun <R> MutableStateFlow<List<R>>.add(others: List<R>) {
            emit((value + others).distinct())
        }
    }
}
