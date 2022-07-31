package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.getString
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

class HomeDrawerRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun openLogin(): LoginRobot<T> {
        composeTest.onLoginNode()
            .performClick()
        return LoginRobot(composeTest)
    }

    fun selectLogin(): HomeDrawerRobot<T> {
        composeTest.onLoginNode()
            .performClick()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit): HomeDrawerRobot<T> =
        also { Verify<T>(composeTest).block() }

    class Verify<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

        fun loginIsDisplayed() {
            composeTest.onLoginNode()
                .assertIsDisplayed()
        }

        fun loginIsNotDisplayed() {
            composeTest.onLoginNode()
                .assertIsNotDisplayed()
        }

        fun loginIsNotSelected() {
            composeTest.onLoginNode()
                .assertIsNotSelected()
        }

        fun appVersionIsDisplayed(version: Int) {
            val appVersion = getString(string.app_version, version)
            composeTest.onNodeWithText(appVersion)
                .assertIsDisplayed()
        }
    }
}

private fun <T : ComponentActivity> AndroidComposeUiTest<T>.onLoginNode(): SemanticsNodeInteraction =
    onNodeWithText(string.home_login)

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeDrawerRobot(content: @Composable () -> Unit) =
    HomeDrawerRobot(this).also { setContent(content) }
