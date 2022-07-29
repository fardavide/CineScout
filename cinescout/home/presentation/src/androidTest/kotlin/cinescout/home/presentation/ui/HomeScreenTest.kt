package cinescout.home.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import cinescout.design.TextRes
import cinescout.home.presentation.model.HomeState
import cinescout.test.compose.runComposeTest
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string
import kotlin.test.Test

class HomeScreenTest {

    @Test
    fun whenSuccessfullyLogin_messageIsShown() = runComposeTest {
        // given
        val state = HomeState.Linked

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onNodeWithText(string.home_logged_in)
            .assertIsDisplayed()
    }

    @Test
    fun whenErrorOnLogin_messageIsShown() = runComposeTest {
        // given
        val message = string.error_no_network
        val state = HomeState.Error(TextRes(message))

        // when
        setContent { HomeScreen(state = state, loginActions = LoginActions.Empty) }

        // then
        onNodeWithText(message)
            .assertIsDisplayed()
    }
}
