package cinescout.search.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.error.NetworkError
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.usecase.AddMovieToLikedList
import cinescout.search.domain.usecase.SearchMovies
import cinescout.search.presentation.model.SearchLikeMovieAction
import cinescout.search.presentation.model.SearchLikedMovieState
import cinescout.search.presentation.previewdata.SearchLikedMoviePreviewData
import cinescout.test.kotlin.TestTimeout
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import store.builder.emptyPagedStore
import store.builder.pagedStoreOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SearchLikedMovieViewModelTest {

    private val addMovieToLikedList: AddMovieToLikedList = mockk(relaxUnitFun = true)
    private val dispatcher = StandardTestDispatcher()
    private val networkErrorToMessageMapper = object : NetworkErrorToMessageMapper() {
        override fun toMessage(networkError: NetworkError) = MessageTextResTestData.NoNetworkError
    }
    private val searchMovies: SearchMovies = mockk {
        every { invoke(query = any()) } returns emptyPagedStore()
    }
    private val viewModel by lazy {
        SearchLikedMovieViewModel(
            addMovieToLikedList = addMovieToLikedList,
            networkErrorToMessageMapper = networkErrorToMessageMapper,
            searchMovies = searchMovies
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `idle state is emitted on start`() = runTest {
        // given
        val expected = SearchLikedMoviePreviewData.Idle

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `loading state is emitted on query change`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = SearchLikedMoviePreviewData.Loading

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeMovieAction.Search(expected.query))
            awaitIdle()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when no results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = SearchLikedMoviePreviewData.NoResults
        every { searchMovies(query = expected.query) } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeMovieAction.Search(expected.query))
            awaitIdle()
            awaitLoading()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when results`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = SearchLikedMoviePreviewData.Query_Inc
        every { searchMovies(query = expected.query) } returns pagedStoreOf(listOf(MovieTestData.Inception))

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeMovieAction.Search(expected.query))
            awaitIdle()
            awaitLoading(expected.query)

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `right state is emitted when error`() = runTest(
        context = dispatcher,
        dispatchTimeoutMs = TestTimeout
    ) {
        // given
        val expected = SearchLikedMoviePreviewData.NoNetwork
        every { searchMovies(query = expected.query) } returns pagedStoreOf(NetworkError.NoNetwork)

        // when
        viewModel.state.test {
            viewModel.submit(SearchLikeMovieAction.Search(expected.query))
            awaitIdle()
            awaitLoading(expected.query)

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<SearchLikedMovieState>.awaitIdle() {
        assertEquals(SearchLikedMoviePreviewData.Idle, awaitItem())
    }

    private suspend fun ReceiveTurbine<SearchLikedMovieState>.awaitLoading(
        query: String = SearchLikedMoviePreviewData.Loading.query
    ) {
        assertEquals(SearchLikedMoviePreviewData.Loading.copy(query = query), awaitItem())
    }
}
