package cinescout.lists.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewData
import cinescout.test.compose.robot.ListRobot
import cinescout.test.compose.robot.ListRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class WatchlistScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.Loading
        ListRobot { WatchlistScreen(state = state) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenError_correctMessageIsDisplayed() = runComposeTest {
        val errorMessage = string.network_error_no_network
        val itemsState = ItemsListState.ItemsState.Error(TextRes(errorMessage))
        ListRobot { WatchlistScreen(itemsState = itemsState) }
            .verify { errorMessageIsDisplayed(errorMessage) }
    }

    @Test
    fun givenAllIsSelected_whenEmptyWatchList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.AllEmptyList
        ListRobot { WatchlistScreen(state = state) }
            .verify { emptyAllWatchlistIsDisplayed() }
    }

    @Test
    fun givenMoviesIsSelected_whenEmptyWatchList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.MoviesEmptyList
        ListRobot { WatchlistScreen(state = state) }
            .verify { emptyMoviesWatchlistIsDisplayed() }
    }

    @Test
    fun givenTvShowsIsSelected_whenEmptyWatchList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.TvShowsEmptyWatchlist
        ListRobot { WatchlistScreen(state = state) }
            .verify { emptyTvShowsWatchlistIsDisplayed() }
    }

    @Composable
    private fun WatchlistScreen(
        itemsState: ItemsListState.ItemsState,
        listType: ListType = ListType.All,
        actions: ItemsListScreen.Actions = ItemsListScreen.Actions.Empty
    ) {
        WatchlistScreen(
            state = ItemsListState(itemsState, listType),
            selectType = { },
            actions = actions
        )
    }

    @Composable
    private fun WatchlistScreen(
        state: ItemsListState,
        actions: ItemsListScreen.Actions = ItemsListScreen.Actions.Empty
    ) {
        WatchlistScreen(
            state = state,
            selectType = { },
            actions = actions
        )
    }
}
