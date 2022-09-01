package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.test.compose.util.getString
import cinescout.test.compose.util.hasText
import studio.forface.cinescout.design.R.string

class HomeDrawerRobot<T : ComponentActivity> internal constructor(private val composeTest: AndroidComposeUiTest<T>) {

    fun openAccounts(): AccountsRobot<T> {
        composeTest.onAccountsNode()
            .performClick()
        return AccountsRobot(composeTest)
    }

    fun openForYou(): ForYouRobot<T> {
        composeTest.onForYouNode()
            .performClick()
        return ForYouRobot(composeTest)
    }

    fun openMyLists(): MyListsRobot<T> {
        composeTest.onMyListsNode()
            .performClick()
        return MyListsRobot(composeTest)
    }

    fun openWatchlist(): WatchlistRobot<T> {
        composeTest.onWatchlistNode()
            .performClick()
        return WatchlistRobot(composeTest)
    }

    fun selectAccounts(): HomeDrawerRobot<T> {
        composeTest.onAccountsNode()
            .performClick()
        return this
    }

    fun selectForYou(): HomeDrawerRobot<T> {
        composeTest.onForYouNode()
            .performClick()
        return this
    }

    fun selectWatchlist(): HomeDrawerRobot<T> {
        composeTest.onWatchlistNode()
            .performClick()
        return this
    }

    fun verify(block: Verify<T>.() -> Unit): HomeDrawerRobot<T> =
        also { Verify(composeTest).block() }

    class Verify<T : ComponentActivity> internal constructor(
        composeTest: AndroidComposeUiTest<T>
    ) : HomeRobot.Verify<T>(composeTest) {

        fun accountsIsDisplayed() {
            composeTest.onAccountsNode()
                .assertIsDisplayed()
        }

        fun accountsIsNotSelected() {
            composeTest.onAccountsNode()
                .assertIsNotSelected()
        }

        fun forYouIsSelected() {
            composeTest.onForYouNode()
                .assertIsSelected()
        }

        fun watchlistIsSelected() {
            composeTest.onWatchlistNode()
                .assertIsSelected()
        }

        fun appVersionIsDisplayed(version: Int) {
            val appVersion = getString(string.app_version, version)
            composeTest.onNodeWithText(appVersion)
                .assertIsDisplayed()
        }
    }
}

private fun <T : ComponentActivity> AndroidComposeUiTest<T>.onAccountsNode(): SemanticsNodeInteraction =
    onAllNodes(hasText(string.home_login) or hasText(string.home_manage_accounts)).onFirst()

private fun <T : ComponentActivity> AndroidComposeUiTest<T>.onForYouNode(): SemanticsNodeInteraction =
    onNode(hasText(string.suggestions_for_you) and isSelectable())

private fun <T : ComponentActivity> AndroidComposeUiTest<T>.onMyListsNode(): SemanticsNodeInteraction =
    onNode(hasText(string.lists_my_lists) and isSelectable())

private fun <T : ComponentActivity> AndroidComposeUiTest<T>.onWatchlistNode(): SemanticsNodeInteraction =
    onNode(hasText(string.lists_watchlist) and isSelectable())

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeDrawerRobot(content: @Composable () -> Unit) =
    HomeDrawerRobot(this).also { setContent(content) }
