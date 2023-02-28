package cinescout.home.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.home.presentation.HomeDestination
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.sample.HomeStateSample.buildHomeState
import cinescout.home.presentation.state.HomeState
import cinescout.test.compose.robot.HomeRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class HomeScreenTest {

    @Test
    fun whenLinkedToTmdb_profilePictureIsShown() = runComposeTest {
        val state = buildHomeState {
            account = HomeStateSample.TmdbAccount
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { profilePictureIsDisplayed() }
    }

    @Test
    fun whenLinkedToTrakt_profilePictureIsShown() = runComposeTest {
        val state = buildHomeState {
            account = HomeStateSample.TraktAccount
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { profilePictureIsDisplayed() }
    }

    @Composable
    fun HomeScreen(state: HomeState) {
        HomeScreen(
            state = state,
            actions = HomeScreen.Actions.Empty,
            startDestination = HomeDestination.None
        )
    }
}
