package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.getString
import cinescout.test.compose.util.hasText
import studio.forface.cinescout.design.R.string

class HomeDrawerRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun openAccounts(): AccountsRobot<T> {
        composeTest.onLoginNode()
            .performClick()
        return AccountsRobot(composeTest)
    }

    fun selectAccounts(): HomeDrawerRobot<T> {
        composeTest.onLoginNode()
            .performClick()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit): HomeDrawerRobot<T> =
        also { Verify<T>(composeTest).block() }

    class Verify<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

        fun accountsIsDisplayed() {
            composeTest.onLoginNode()
                .assertIsDisplayed()
        }

        fun accountsIsNotSelected() {
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
    onAllNodes(hasText(string.home_login) or hasText(string.home_manage_accounts)).onFirst()

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeDrawerRobot(content: @Composable () -> Unit) =
    HomeDrawerRobot(this).also { setContent(content) }
