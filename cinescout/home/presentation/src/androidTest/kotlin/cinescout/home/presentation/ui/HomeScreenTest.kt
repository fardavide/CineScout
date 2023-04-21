package cinescout.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.runComposeUiTest
import cinescout.home.presentation.HomeDestination
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.sample.HomeStateSample.buildHomeState
import cinescout.home.presentation.state.HomeState
import cinescout.test.compose.robot.HomeRobot
import kotlin.test.Test

class HomeScreenTest {

    @Test
    fun whenLinkedToTrakt_profilePictureIsShown() = runComposeUiTest {
        val state = buildHomeState {
            account = HomeStateSample.Account
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
