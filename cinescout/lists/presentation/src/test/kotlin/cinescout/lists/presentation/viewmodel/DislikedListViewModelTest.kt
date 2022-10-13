package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ListItemUiModelPreviewData
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.usecase.GetAllDislikedMovies
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

class DislikedListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val getAllDislikedMovies: GetAllDislikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>())
    }
    private val listItemUiModelMapper = ListItemUiModelMapper()
    private val viewModel by lazy {
        DislikedListViewModel(
            getAllDislikedMovies = getAllDislikedMovies,
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
    fun `emits empty list when no disliked movies`() = runTest(dispatcher) {
        // given
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.Empty,
            type = ListType.All
        )
        every { getAllDislikedMovies() } returns flowOf(emptyList())

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits disliked movies`() = runTest(dispatcher) {
        // given
        val models = nonEmptyListOf(
            ListItemUiModelPreviewData.Inception.copy(personalRating = null)
        )
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.NotEmpty(models),
            type = ListType.All
        )
        every { getAllDislikedMovies() } returns flowOf(listOf(MovieTestData.Inception))

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
