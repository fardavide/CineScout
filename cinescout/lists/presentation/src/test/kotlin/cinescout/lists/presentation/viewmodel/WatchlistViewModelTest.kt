package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageSample
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ListItemUiModelPreviewData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import store.builder.emptyPagedStore
import store.builder.pagedStoreOf
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchlistViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val errorToMessageMapper: NetworkErrorToMessageMapper = mockk {
        every { toMessage(any()) } returns MessageSample.NoNetworkError
    }
    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
        every { this@mockk(refresh = any()) } returns emptyPagedStore()
    }
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows = mockk {
        every { this@mockk(refresh = any()) } returns emptyPagedStore()
    }
    private val listItemUiModelMapper = ListItemUiModelMapper()
    private val viewModel by lazy {
        WatchlistViewModel(
            errorToMessageMapper = errorToMessageMapper,
            getAllWatchlistMovies = getAllWatchlistMovies,
            getAllWatchlistTvShows = getAllWatchlistTvShows,
            listItemUiModelMapper = listItemUiModelMapper
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits loading at start`() = runTest(dispatcher) {
        // given
        val expected = ItemsListState.Loading

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits empty watchlist when no movies in watchlist`() = runTest(dispatcher) {
        // given
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.Empty,
            type = ListType.All
        )
        every { getAllWatchlistMovies() } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits movies from watchlist`() = runTest(dispatcher) {
        // given
        val models = nonEmptyListOf(
            ListItemUiModelPreviewData.Inception.copy(personalRating = null)
        )
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.NotEmpty(models),
            type = ListType.All
        )
        every { getAllWatchlistMovies(refresh = any()) } returns pagedStoreOf(MovieTestData.Inception)

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<ItemsListState>.givenLoadingEmitted() {
        assertEquals(ItemsListState.Loading, awaitItem())
    }
}
