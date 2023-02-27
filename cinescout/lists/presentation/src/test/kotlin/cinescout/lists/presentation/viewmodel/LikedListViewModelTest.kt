package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ListItemUiModelPreviewData
import cinescout.lists.presentation.state.ItemsListState
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LikedListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val getAllLikedMovies: GetAllLikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>())
    }
    private val getAllLikedTvShows: GetAllLikedTvShows = mockk {
        every { this@mockk() } returns flowOf(emptyList<TvShow>())
    }
    private val listItemUiModelMapper = ListItemUiModelMapper()
    private val viewModel by lazy {
        LikedListViewModel(
            getAllLikedMovies = getAllLikedMovies,
            getAllLikedTvShows = getAllLikedTvShows,
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
    fun `emits empty list when no liked movies`() = runTest(dispatcher) {
        // given
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.Empty,
            filter = ListFilter.Disliked,
            type = ListType.All
        )
        every { getAllLikedMovies() } returns flowOf(emptyList())

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits liked movies`() = runTest(dispatcher) {
        // given
        val models = nonEmptyListOf(
            ListItemUiModelPreviewData.Inception.copy(personalRating = null)
        )
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.NotEmpty(models),
            filter = ListFilter.Disliked,
            type = ListType.All
        )
        every { getAllLikedMovies() } returns flowOf(listOf(MovieSample.Inception))

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
