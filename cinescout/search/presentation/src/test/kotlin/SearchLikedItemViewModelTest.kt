package cinescout.search.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageSample
import cinescout.error.NetworkError
import cinescout.movies.domain.sample.MovieSample
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.previewdata.SearchLikedItemPreviewData
import cinescout.search.presentation.reducer.SearchLikedItemReducer
import cinescout.test.kotlin.TestTimeoutMs
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.usecase.AddTvShowToLikedList
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import store.builder.pagedStoreOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SearchLikedItemViewModelTest {

    private val addTvShowToLikedList: AddTvShowToLikedList = mockk(relaxUnitFun = true)
    private val dispatcher = StandardTestDispatcher()
    private val networkErrorToMessageMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageSample.NoNetworkError
    }
    private val reducer = SearchLikedItemReducer()
    private val viewModel by lazy {
        SearchLikedItemViewModel(
            networkErrorToMessageMapper = networkErrorToMessageMapper,
            reducer = reducer,
            searchScreenplays = searchMovies,
            searchTvShows = searchTvShows
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `idle state is emitted on start`() = runTest {
        // given
        val expected = SearchLikedItemPreviewData.Idle

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `loading state is emitted on query change`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.InitialLoading

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `right state is emitted when no movies results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.NoResults
        every { searchMovies(query = expected.query) } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when no tv shows results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.NoResults
        viewModel.submit(SearchLikeItemAction.SelectItemType(SearchLikedItemType.TvShows))
        every { searchTvShows(query = expected.query) } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when movies results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.QueryMovies_Inc
        every { searchMovies(query = expected.query) } returns pagedStoreOf(listOf(MovieSample.Inception))

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()
            awaitLoading(expected.query)

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when tv shows results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.QueryTvShows_Gri
        viewModel.submit(SearchLikeItemAction.SelectItemType(SearchLikedItemType.TvShows))
        every { searchTvShows(query = expected.query) } returns pagedStoreOf(listOf(TvShowSample.Grimm))

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()
            awaitLoading(expected.query)

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when error`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = SearchLikedItemPreviewData.NoNetwork
        every { searchMovies(query = expected.query) } returns pagedStoreOf(NetworkError.NoNetwork)

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeItemAction.Search(expected.query))
            awaitIdle()
            awaitLoading(expected.query)

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<SearchLikedItemState>.awaitIdle() {
        assertEquals(SearchLikedItemPreviewData.Idle, awaitItem())
    }

    private suspend fun ReceiveTurbine<SearchLikedItemState>.awaitLoading(
        query: String = SearchLikedItemPreviewData.InitialLoading.query
    ) {
        assertEquals(SearchLikedItemPreviewData.InitialLoading.copy(query = query), awaitItem())
    }
}
