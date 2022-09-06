package cinescout.lists.presentation.ui

import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import cinescout.test.compose.robot.ListRobot
import cinescout.test.compose.robot.ListRobot.Companion.verify
import cinescout.test.compose.runComposeTest
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class RatedListScreenTest {

    @Test
    fun whenLoading_progressIsDisplayed() = runComposeTest {
        val state = ItemsListState.Loading
        ListRobot { RatedListScreen(state = state) }
            .verify { progressIsDisplayed() }
    }

    @Test
    fun whenError_correctMessageIsDisplayed() = runComposeTest {
        val errorMessage = string.network_error_no_network
        val state = ItemsListState.Error(TextRes(errorMessage))
        ListRobot { RatedListScreen(state = state) }
            .verify { errorMessageIsDisplayed(errorMessage) }
    }

    @Test
    fun whenEmptyWatchList_emptyWatchlistIsDisplayed() = runComposeTest {
        val state = ItemsListState.Data.Empty
        ListRobot { RatedListScreen(state = state) }
            .verify { noRatedMoviesIsDisplayed() }
    }
}
