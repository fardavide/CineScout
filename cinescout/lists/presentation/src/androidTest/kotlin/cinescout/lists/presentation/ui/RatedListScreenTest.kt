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

class RatedListScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeTest {
        val itemsState = ItemsListState.ItemsState.Loading
        ListRobot { RatedListScreen(itemsState = itemsState) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenError_correctMessageIsDisplayed() = runComposeTest {
        val errorMessage = string.network_error_no_network
        val itemsState = ItemsListState.ItemsState.Error(TextRes(errorMessage))
        ListRobot { RatedListScreen(itemsState = itemsState) }
            .verify { errorMessageIsDisplayed(errorMessage) }
    }

    @Test
    fun givenAllIsSelected_whenEmptyRatedList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.AllEmptyList
        ListRobot { RatedListScreen(state = state) }
            .verify { emptyAllRatedListIsDisplayed() }
    }

    @Test
    fun givenMoviesIsSelected_whenEmptyRatedList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.MoviesEmptyList
        ListRobot { RatedListScreen(state = state) }
            .verify { emptyMoviesRatedListIsDisplayed() }
    }

    @Test
    fun givenTvShowsIsSelected_whenEmptyRatedList_correctMessageIsDisplayed() = runComposeTest {
        val state = ItemsListScreenPreviewData.TvShowsEmptyWatchlist
        ListRobot { RatedListScreen(state = state) }
            .verify { emptyTvShowsRatedListIsDisplayed() }
    }

    @Composable
    private fun RatedListScreen(
        state: ItemsListState,
        actions: ItemsListScreen.Actions = ItemsListScreen.Actions.Empty
    ) {
        RatedListScreen(state = state, actions = actions, selectType = {})
    }

    @Composable
    private fun RatedListScreen(
        itemsState: ItemsListState.ItemsState,
        actions: ItemsListScreen.Actions = ItemsListScreen.Actions.Empty
    ) {
        RatedListScreen(state = ItemsListState(itemsState, ListType.All), actions = actions, selectType = {})
    }
}
