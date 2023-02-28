package cinescout.test.compose.robot

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.test.compose.semantic.ForYouSemantics
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ItemsListSemantics
import cinescout.test.compose.semantic.ManageAccountSemantics
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onAllNodesWithContentDescription
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest, HomeSemantics)
open class HomeRobot {

    fun openForYou(): ForYouRobot {
        forYou().performClick()
        return ForYouSemantics { ForYouRobot() }
    }

    fun openMyLists(): ItemsListRobot {
        myLists().performClick()
        return ItemsListSemantics { ItemsListRobot() }
    }

    fun openProfile(): ManageAccountRobot {
        profile().performClick()
        return ManageAccountSemantics { ManageAccountRobot() }
    }

    fun verify(block: Verify.() -> Unit): HomeRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, HomeSemantics)
    open class Verify internal constructor() {

        fun bannerIsDisplayed(message: TextRes) {
            onNode(hasParent(hasTestTag(TestTag.Banner)) and hasText(message))
                .assertIsDisplayed()
        }

        fun errorMessageIsDisplayed(@StringRes message: Int) {
            onNodeWithText(message).assertIsDisplayed()
        }

        fun errorMessageIsDisplayed(textRes: TextRes) {
            onNodeWithText(textRes).assertIsDisplayed()
        }

        fun forYouIsDisplayed() {
            onNodeWithTag(TestTag.ForYou).assertIsDisplayed()
        }

        fun likedIsDisplayed() {
            onNodeWithTag(TestTag.Liked).assertIsDisplayed()
        }

        fun loggedInSnackbarIsDisplayed() {
            onNodeWithText(string.manage_account_logged_in).assertIsDisplayed()
        }

        fun myListsIsDisplayed() {
            onNodeWithTag(TestTag.MyLists).assertIsDisplayed()
        }

        fun profilePictureIsDisplayed() {
            onAllNodesWithContentDescription(string.profile_picture_description)
                .also { it.onFirst().awaitDisplayed() }
                .assertCountEquals(2)
        }

        fun progressIsDisplayed() {
            onNodeWithTag(TestTag.Progress).assertIsDisplayed()
        }

        fun ratedIsDisplayed() {
            onNodeWithTag(TestTag.Rated).assertIsDisplayed()
        }

        fun searchLikedIsDisplayed() {
            onNodeWithTag(TestTag.SearchLiked).assertIsDisplayed()
        }

        fun watchlistIsDisplayed() {
            onNodeWithTag(TestTag.Watchlist).assertIsDisplayed()
        }

    }
}

context(ComposeUiTest)
fun HomeRobot(content: @Composable () -> Unit): HomeRobot {
    setContent(content)
    return HomeSemantics { HomeRobot() }
}
