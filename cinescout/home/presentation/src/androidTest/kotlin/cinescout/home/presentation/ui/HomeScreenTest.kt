package cinescout.home.presentation.ui

import androidx.compose.runtime.Composable
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.home.presentation.HomeDestination
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.sample.HomeStateSample.HomeStateBuilder.LoginError
import cinescout.home.presentation.sample.HomeStateSample.buildHomeState
import cinescout.home.presentation.state.HomeState
import cinescout.test.compose.robot.HomeRobot
import cinescout.test.compose.runComposeTest
import kotlin.test.Test

class HomeScreenTest {

    @Test
    fun whenSuccessfullyLogin_messageIsShown() = runComposeTest {
        val state = buildHomeState {
            login = HomeState.Login.Linked
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { loggedInSnackbarIsDisplayed() }
    }

    @Test
    fun whenSuccessfullyLoginToTmdb_profilePictureIsShown() = runComposeTest {
        val state = buildHomeState {
            accounts {
                tmdb = HomeStateSample.TmdbAccount
            }
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { profilePictureIsDisplayed() }
    }

    @Test
    fun whenSuccessfullyLoginToTrakt_profilePictureIsShown() = runComposeTest {
        val state = buildHomeState {
            accounts {
                trakt = HomeStateSample.TraktAccount
            }
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { profilePictureIsDisplayed() }
    }

    @Test
    fun whenErrorOnLogin_messageIsShown() = runComposeTest {
        val message = string.network_error_no_network
        val state = buildHomeState {
            login = TextRes(message) `as` LoginError
        }
        HomeRobot { HomeScreen(state = state) }
            .verify { errorMessageIsDisplayed(message) }
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
