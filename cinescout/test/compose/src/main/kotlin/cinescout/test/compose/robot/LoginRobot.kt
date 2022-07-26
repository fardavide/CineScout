package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class LoginRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

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
