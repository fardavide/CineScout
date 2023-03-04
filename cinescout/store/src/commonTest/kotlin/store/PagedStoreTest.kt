package store

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.test.kotlin.TestTimeoutMs
import com.soywiz.klock.DateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import store.builder.remotePagedDataOf
import store.builder.toLocalPagedData
import store.builder.toRemotePagedData
import store.test.MockStoreOwner
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PagedStoreTest {

    private val owner = MockStoreOwner()

    @Test
    fun `emits from local data, then refresh from remote`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1)
        val page = Paging.Page(page = 2, totalPages = 2)
        val remoteData = loadRemoteData(page)
        val expectedUpdatedData = listOf(1, 2).toRemotePagedData(page).right()

        val localFlow: MutableStateFlow<List<Int>> =
            MutableStateFlow(localData)

        val store = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            assertEquals(localData.toLocalPagedData().right(), awaitItem())
            assertEquals(listOf(1, 2).toLocalPagedData().right(), awaitItem())
            assertEquals(expectedUpdatedData, awaitItem())
        }
    }

    @Test
    fun `paged store returns local data updated from another source, after error`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val networkError = NetworkOperation.Error(NetworkError.NoNetwork)
        val dataFromAnotherSource = listOf(2)
        val pagedDataFromAnotherSource = listOf(2).toLocalPagedData().right()
        val expectedError = DataError.Remote(networkError = networkError.error).left()

        val localFlow: MutableStateFlow<List<Int>> =
            MutableStateFlow(emptyList())

        val store = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            launch { localFlow.emit(dataFromAnotherSource) }
            owner.updated()
            assertEquals(pagedDataFromAnotherSource, awaitItem())
        }
    }

    @Test
    fun `does emit local data if updated and network call is skipped`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2)
        val store = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            assertEquals(localData.toLocalPagedData().right(), awaitItem())
            assertEquals(emptyList(), cancelAndConsumeRemainingEvents())
        }
    }

    @Test
    fun `does emit local data if data if not updated and network call is skipped`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2)
        val store = owner.fresh().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            assertEquals(localData.toLocalPagedData().right(), awaitItem())
            assertEquals(emptyList(), cancelAndConsumeRemainingEvents())
        }
    }

    @Test
    fun `paged store filter all intermediate pages`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
        // given
        val totalPages = 3
        val expected = buildLocalData(totalPages, totalPages = totalPages).right()
        val localData = listOf(1)

        val localFlow = MutableStateFlow(localData)

        val flow = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
    fun `paged store loads more`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
        // given
        val localData = listOf(1)
        val localPagedData = localData.toLocalPagedData().right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            assertEquals(listOf(1, 2).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(2).right(), awaitItem())
            store.loadMore()
            assertEquals(listOf(1, 2, 3).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(3).right(), awaitItem())
        }
    }

    @Test
    fun `paged store loads all`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
        // given
        val localData = listOf(1)
        val localPagedData = localData.toLocalPagedData().right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
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
            assertEquals(listOf(1, 2).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(2).right(), awaitItem())
            assertEquals(listOf(1, 2, 3).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(3).right(), awaitItem())
            assertEquals(listOf(1, 2, 3, 4).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(4).right(), awaitItem())
            assertEquals(listOf(1, 2, 3, 4, 5).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(5).right(), awaitItem())
        }
    }

    @Test
    fun `paged store get all`() = runTest(dispatchTimeoutMs = TestTimeoutMs) {
        // given
        val localData = listOf(1)
        val totalPages = 5
        val expected = buildLocalData(totalPages).data.right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = totalPages))
            },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when - then
        assertEquals(expected, store.getAll())
    }

    @Test
    fun `do not refresh when refresh is if expired and local data is available`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1)
        val remoteData = remotePagedDataOf(2).right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            refresh = Refresh.IfExpired(),
            fetch = { remoteData },
            write = { localFlow.add(it) },
            read = { flowOf(localData) }
        )

        // when
        store.test {

            // then
            assertEquals(localData.toRemotePagedData(Paging.Page.Initial).right(), awaitItem())
            advanceUntilIdle()
            assertEquals(emptyList(), cancelAndConsumeRemainingEvents())
        }
    }

    @Test
    fun `refresh when refresh is if expired and local data is available, but expired`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1)
        val remoteData = remotePagedDataOf(2).right()

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.expired().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            refresh = Refresh.IfExpired(),
            fetch = { remoteData },
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.test {

            // then
            assertEquals(remotePagedDataOf(1, 2).right(), awaitItem())
            advanceUntilIdle()
            assertEquals(emptyList(), cancelAndConsumeRemainingEvents())
        }
    }

    @Test
    fun `does skip pages up to date and refresh others, when refresh mode is expired`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2)
        val localFlow = MutableStateFlow(localData)
        val totalPages = 4

        fun page(page: Int) = Paging.Page(
            page = page,
            totalPages = totalPages
        )

        val owner = owner.fresh().apply {
            val fetchData = FetchData(DateTime.now())
            saveFetchData(TestKey.paged(page(1)).value(), fetchData)
            saveFetchData(TestKey.paged(page(2)).value(), fetchData)
        }

        val fetchedPages = mutableListOf<Paging.Page>()
        val fetch: suspend (page: Paging.Page) ->
        Either<NetworkOperation, PagedData.Remote<Int>> = { page ->
            fetchedPages.add(page)
            loadRemoteData(page.page, totalPages = totalPages)
        }

        val store: PagedStore<Int, Paging> = owner.PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            refresh = Refresh.IfExpired(),
            fetch = fetch,
            write = { localFlow.add(it) },
            read = { localFlow }
        )

        // when
        store.loadAll().test {

            // then
            assertEquals(remotePagedDataOf(1, 2, paging = page(1)).right(), awaitItem())
            assertEquals(remotePagedDataOf(1, 2, paging = page(2)).right(), awaitItem())
            assertEquals(remotePagedDataOf(1, 2, 3, paging = page(3)).right(), awaitItem())
            assertEquals(remotePagedDataOf(1, 2, 3, 4, paging = page(4)).right(), awaitItem())
            assertEquals(listOf(Paging.Page.Initial, page(3), page(4)), fetchedPages)
        }
    }

    @Test
    fun `delete outdated local data when all pages are fetched`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2, 3, 4)
        val totalPages = 3
        val expectedLocal = listOf(1, 2, 3)

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = totalPages))
            },
            write = { localFlow.add(it) },
            read = { localFlow },
            delete = { localFlow.remove(it) }
        )

        // when
        store.loadAll().test {

            assertEquals(localData.toLocalPagedData().right(), awaitItem())
            assertEquals(localData.toRemotePagedData(Paging.Page(1, totalPages)).right(), awaitItem())
            assertEquals(localData.toRemotePagedData(Paging.Page(2, totalPages)).right(), awaitItem())

            // then
            assertEquals(listOf(1, 2, 3).toLocalPagedData().right(), awaitItem())
            assertEquals(buildLocalData(totalPages, totalPages = totalPages).right(), awaitItem())
            assertEquals(expectedLocal, localFlow.value)
        }
    }

    @Test
    fun `does not delete local data if all pages are not fetched`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2, 3, 4)
        val totalPages = 3

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page.Initial,
            fetch = { page ->
                delay(NetworkDelay)
                loadRemoteData(page.copy(totalPages = totalPages))
            },
            write = { localFlow.add(it) },
            read = { localFlow },
            delete = { localFlow.remove(it) }
        )

        // when
        store.test {

            assertEquals(localData.toLocalPagedData().right(), awaitItem())
            assertEquals(localData.toRemotePagedData(Paging.Page(1, totalPages)).right(), awaitItem())

            store.loadMore()
            assertEquals(localData.toRemotePagedData(Paging.Page(2, totalPages)).right(), awaitItem())

            // then
            assertEquals(localData, localFlow.value)
        }
    }

    @Test
    fun `does not fetch for invalid page`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val localData = listOf(1, 2)

        val localFlow = MutableStateFlow(localData)

        val store: PagedStore<Int, Paging> = owner.updated().PagedStore(
            key = TestKey,
            initialPage = Paging.Page(3, 2),
            fetch = { page -> loadRemoteData(page) },
            write = { localFlow.add(it) },
            read = { localFlow },
            delete = { localFlow.remove(it) }
        )

        // when
        store.test {

            assertEquals(localData.toLocalPagedData().right(), awaitItem())

            store.loadMore()

            // then
            assertEquals(localData, localFlow.value)
        }
    }

    private companion object {

        const val NetworkDelay = 100L
        val TestKey = StoreKey("test")

        fun loadRemoteData(page: Int, totalPages: Int = 5): Either<Nothing, PagedData.Remote<Int>> {
            if (page > totalPages) throw IllegalStateException("Page $page is too high")
            return PagedData.Remote(
                data = listOf(page),
                paging = Paging.Page(page = page, totalPages = totalPages)
            ).right()
        }

        fun loadRemoteData(page: Paging.Page): Either<Nothing, PagedData.Remote<Int>> = PagedData.Remote(
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

        private suspend fun <R> MutableStateFlow<List<R>>.remove(others: List<R>) {
            emit(value - others.toSet())
        }
    }
}
