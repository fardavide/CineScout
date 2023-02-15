package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.design.R.string
import cinescout.test.compose.semantic.ForYouSemantics
import cinescout.test.compose.semantic.HomeDrawerSemantics
import cinescout.test.compose.semantic.ListSemantics
import cinescout.test.compose.semantic.MyListsSemantics
import cinescout.test.compose.util.getString

context(ComposeUiTest, HomeDrawerSemantics)
class HomeDrawerRobot internal constructor() {

    fun openAccounts(): AccountsRobot {
        accounts().performClick()
        return AccountsRobot()
    }

    fun openForYou(): ForYouRobot {
        forYou().performClick()
        return ForYouSemantics { ForYouRobot() }
    }

    fun openMyLists(): MyListsRobot {
        myLists().performClick()
        return MyListsSemantics { MyListsRobot() }
    }

    fun openWatchlist(): ListRobot {
        watchlist().performClick()
        return ListSemantics { ListRobot() }
    }

    fun selectAccounts(): HomeDrawerRobot {
        accounts().performClick()
        return this
    }

    fun selectWatchlist(): HomeDrawerRobot {
        watchlist().performClick()
        return this
    }

    fun verify(block: Verify.() -> Unit): HomeDrawerRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, HomeDrawerSemantics)
    class Verify internal constructor() {

        fun accountsIsDisplayed() {
            accounts().assertIsDisplayed()
        }

        fun accountsIsNotSelected() {
            accounts().assertIsNotSelected()
        }

        fun forYouIsSelected() {
            forYou().assertIsSelected()
        }

        fun watchlistIsSelected() {
            watchlist().assertIsSelected()
        }

        fun appVersionIsDisplayed(version: Int) {
            val appVersion = getString(string.app_version, version)
            onNodeWithText(appVersion)
                .assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun HomeDrawerRobot(content: @Composable () -> Unit): HomeDrawerRobot {
    setContent(content)
    return HomeDrawerSemantics { HomeDrawerRobot() }
}
