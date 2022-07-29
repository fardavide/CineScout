package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.onNodeWithContentDescription
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class LoginRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun closeLogin(): HomeRobot<T> {
        composeTest.onNodeWithContentDescription(string.close_button_description)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun selectTmdb(): HomeRobot<T> {
        composeTest.onNodeWithText(string.home_login_to_tmdb)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun selectTrakt(): HomeRobot<T> {
        composeTest.onNodeWithText(string.home_login_to_trakt)
            .performClick()
        return HomeRobot(composeTest)
    }

    fun verify(block: Verify<T>.() -> Unit): LoginRobot<T> =
        also { Verify(composeTest).block() }

    class Verify<T : ComponentActivity>(private val composeTest: AndroidComposeUiTest<T>) {

        fun loginToTmdbIsDisplayed() {
            composeTest.onNodeWithText(string.home_login_to_tmdb)
                .assertIsDisplayed()
        }

        fun loginToTraktIsDisplayed() {
            composeTest.onNodeWithText(string.home_login_to_trakt)
                .assertIsDisplayed()
        }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.LoginRobot(content: @Composable () -> Unit) =
    LoginRobot(this).also { setContent(content) }
