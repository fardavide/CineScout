package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onAllNodesWithContentDescription
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

open class HomeRobot<T : ComponentActivity>(val composeTest: AndroidComposeUiTest<T>) {

    fun openDrawer(): HomeDrawerRobot<T> {
        composeTest.onRoot().performTouchInput { swipeRight() }
        return HomeDrawerRobot(composeTest)
    }

    open class Verify<T : ComponentActivity> internal constructor(protected val composeTest: AndroidComposeUiTest<T>) {

        fun drawerIsClosed() {
            composeTest.onNodeWithTag(TestTag.Drawer)
            // TODO: how to verify it's closed?
        }

        fun drawerIsOpen() {
            composeTest.onNodeWithTag(TestTag.Drawer)
                .assertIsDisplayed()
        }

        fun errorMessageIsDisplayed(@StringRes message: Int) {
            composeTest.onNodeWithText(message)
                .assertIsDisplayed()
        }

        fun forYouIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.ForYou)
                .assertIsDisplayed()
        }

        fun forYouSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.suggestions_for_you) and isSelectable().not())
                .assertIsDisplayed()
        }

        fun loggedInSnackbarIsDisplayed() {
            composeTest.onNodeWithText(string.home_logged_in)
                .assertIsDisplayed()
        }

        fun profilePictureIsDisplayed() {
            composeTest.onAllNodesWithContentDescription(string.profile_picture_description)
                .assertCountEquals(2)
        }

        fun progressIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.Progress)
                .assertIsDisplayed()
        }

        fun watchlistIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.Watchlist)
                .assertIsDisplayed()
        }

        fun watchlistSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.lists_watchlist) and isSelectable().not())
                .assertIsDisplayed()
        }
    }

    companion object {

        fun <T : ComponentActivity> HomeRobot<T>.verify(block: HomeRobot.Verify<T>.() -> Unit): HomeRobot<T> =
            also { HomeRobot.Verify<T>(composeTest).block() }
    }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.HomeRobot(content: @Composable () -> Unit) =
    HomeRobot(this).also { setContent(content) }
