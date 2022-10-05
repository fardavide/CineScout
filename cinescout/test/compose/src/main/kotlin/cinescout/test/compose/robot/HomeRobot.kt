package cinescout.test.compose.robot

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onAllNodesWithContentDescription
import cinescout.test.compose.util.onNodeWithText
import studio.forface.cinescout.design.R.string

open class HomeRobot<T : ComponentActivity>(val composeTest: AndroidComposeUiTest<T>) {

    fun asForYou() = ForYouRobot(composeTest)

    fun openDrawer(): HomeDrawerRobot<T> {
        composeTest.onNodeWithTag(TestTag.BottomBar).performTouchInput { swipeRight() }
        return HomeDrawerRobot(composeTest)
    }

    open class Verify<T : ComponentActivity> internal constructor(protected val composeTest: AndroidComposeUiTest<T>) {

        fun dislikedIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.Disliked)
                .assertIsDisplayed()
        }

        fun dislikedSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.lists_disliked) and isSelectable().not())
                .assertIsDisplayed()
        }

        fun drawerIsClosed() {
            composeTest.onNodeWithTag(TestTag.Drawer)
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

        fun likedIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.Liked)
                .assertIsDisplayed()
        }

        fun likedSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.lists_liked) and isSelectable().not())
                .assertIsDisplayed()
        }

        fun loggedInSnackbarIsDisplayed() {
            composeTest.onNodeWithText(string.home_logged_in)
                .assertIsDisplayed()
        }

        fun myListsIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.MyLists)
                .assertIsDisplayed()
        }

        fun myListSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.lists_my_lists) and isSelectable().not())
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

        fun ratedIsDisplayed() {
            composeTest.onNodeWithTag(TestTag.Rated)
                .assertIsDisplayed()
        }

        fun ratedSubtitleIsDisplayed() {
            composeTest.onNode(hasText(string.lists_rated) and isSelectable().not())
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
