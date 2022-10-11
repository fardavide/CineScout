package cinescout.lists.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import cinescout.test.compose.robot.ListRobot
import cinescout.test.compose.robot.ListRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class WatchlistScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeTest {
        val itemsState = ItemsListState.ItemsState.Loading
        ListRobot { WatchlistScreen(itemsState = itemsState) }
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
    fun whenEmptyWatchList_emptyWatchlistIsDisplayed() = runComposeTest {
        val itemsState = ItemsListState.ItemsState.Data.Empty
        ListRobot { WatchlistScreen(itemsState = itemsState) }
            .verify { emptyWatchlistIsDisplayed() }
    }

    @Composable
    private fun WatchlistScreen(
        itemsState: ItemsListState.ItemsState,
        actions: ItemsListScreen.Actions = ItemsListScreen.Actions.Empty
    ) {
        WatchlistScreen(state = ItemsListState(itemsState, ItemsListState.Type.All), actions = actions)
    }
}
