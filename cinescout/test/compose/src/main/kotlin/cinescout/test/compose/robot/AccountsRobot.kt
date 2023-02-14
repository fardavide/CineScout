package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.design.R.string
import cinescout.test.compose.util.getString
import cinescout.test.compose.util.onNodeWithContentDescription
import cinescout.test.compose.util.onNodeWithText

class AccountsRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun close(): HomeRobot<T> {
        composeTest.onNodeWithContentDescription(string.close_button_description)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun selectTmdb(): HomeRobot<T> {
        composeTest.onNodeWithText(string.home_connect_to_tmdb)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun selectTrakt(): HomeRobot<T> {
        composeTest.onNodeWithText(string.home_connect_to_trakt)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun verify(block: Verify<T>.() -> Unit): AccountsRobot<T> =
        also { Verify(composeTest).block() }

    class Verify<T : ComponentActivity>(private val composeTest: AndroidComposeUiTest<T>) {

        fun connectToTmdbIsDisplayed() {
            composeTest.onNodeWithText(string.home_connect_to_tmdb)
                .assertIsDisplayed()
        }

        fun connectToTraktIsDisplayed() {
            composeTest.onNodeWithText(string.home_connect_to_trakt)
                .assertIsDisplayed()
        }

        fun tmdbUsernameIsDisplayed(username: String) {
            composeTest.onNodeWithText(getString(string.home_connected_to_tmdb_as, username))
                .assertIsDisplayed()
        }

        fun traktUsernameIsDisplayed(username: String) {
            composeTest.onNodeWithText(getString(string.home_connected_to_trakt_as, username))
                .assertIsDisplayed()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.AccountsRobot(content: @Composable () -> Unit) =
    AccountsRobot(this).also { setContent(content) }
