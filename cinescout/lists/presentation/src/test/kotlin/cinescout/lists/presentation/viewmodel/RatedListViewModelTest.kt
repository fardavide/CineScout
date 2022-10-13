package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ListItemUiModelPreviewData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.movies.domain.usecase.GetAllRatedMovies
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

class RatedListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val errorToMessageMapper: NetworkErrorToMessageMapper = mockk {
        every { toMessage(any()) } returns MessageTextResTestData.NoNetworkError
    }
    private val getAllRatedMovies: GetAllRatedMovies = mockk {
        every { this@mockk(refresh = any()) } returns emptyPagedStore()
    }
    private val listItemUiModelMapper = ListItemUiModelMapper()
    private val viewModel by lazy {
        RatedListViewModel(
            errorToMessageMapper = errorToMessageMapper,
            getAllRatedMovies = getAllRatedMovies,
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
    fun `emits empty list when no rated movies`() = runTest(dispatcher) {
        // given
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.Empty,
            type = ListType.All
        )
        every { getAllRatedMovies() } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits rated movies`() = runTest(dispatcher) {
        // given
        val models = nonEmptyListOf(
            ListItemUiModelPreviewData.Inception
        )
        val expected = ItemsListState(
            items = ItemsListState.ItemsState.Data.NotEmpty(models),
            type = ListType.All
        )
        every { getAllRatedMovies(refresh = any()) } returns pagedStoreOf(MovieWithPersonalRatingTestData.Inception)

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
