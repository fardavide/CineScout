package cinescout.home.presentation.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.design.TextRes
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.testdata.HomeStateTestData
import cinescout.home.presentation.testdata.HomeStateTestData.HomeStateBuilder.LoginError
import cinescout.home.presentation.testdata.HomeStateTestData.buildHomeState
import cinescout.test.compose.runComposeTest
import cinescout.test.compose.util.onAllNodesWithContentDescription
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class HomeScreenTest {

    @Test
    fun whenSuccessfullyLogin_messageIsShown() = runComposeTest {
        // given
        val state = buildHomeState {
            login = HomeState.Login.Linked
        }

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onNodeWithText(string.home_logged_in)
            .assertIsDisplayed()
    }

    @Test
    fun whenSuccessfullyLoginToTmdb_profilePictureIsShown() = runComposeTest {
        // given
        val state = buildHomeState {
            accounts {
                tmdb = HomeStateTestData.TmdbAccount
            }
        }

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onAllNodesWithContentDescription(string.profile_picture_description)
            .assertCountEquals(2)
    }

    @Test
    fun whenSuccessfullyLoginToTrakt_profilePictureIsShown() = runComposeTest {
        // given
        val state = buildHomeState {
            accounts {
                trakt = HomeStateTestData.TraktAccount
            }
        }

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onAllNodesWithContentDescription(string.profile_picture_description)
            .assertCountEquals(2)
    }

    @Test
    fun whenErrorOnLogin_messageIsShown() = runComposeTest {
        // given
        val message = string.network_error_no_network
        val state = buildHomeState {
            login = TextRes(message) `as` LoginError
        }

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onNodeWithText(message)
            .assertIsDisplayed()
    }
}
