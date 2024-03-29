package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import cinescout.design.TestTag
import cinescout.resources.TextRes
import cinescout.test.compose.semantic.ForYouSemantics
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ItemsListSemantics
import cinescout.test.compose.semantic.ProfileSemantics
import cinescout.test.compose.semantic.SearchSemantics
import cinescout.test.compose.util.awaitDisplayed
import cinescout.test.compose.util.hasText
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

    fun openProfile(): ProfileRobot {
        profile().performClick()
        return ProfileSemantics { ProfileRobot() }
    }

    fun openSearch(): SearchRobot {
        search().performClick()
        return SearchSemantics { SearchRobot() }
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

        fun errorMessageIsDisplayed(textRes: TextRes) {
            onNodeWithText(textRes).assertIsDisplayed()
        }

        fun profilePictureIsDisplayed() {
            profilePicture()
                .awaitDisplayed()
                .assertIsDisplayed()
        }

        fun progressIsDisplayed() {
            onNodeWithTag(TestTag.Progress).assertIsDisplayed()
        }

        fun searchLikedIsDisplayed() {
            onNodeWithTag(TestTag.SearchLiked).assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun HomeRobot(content: @Composable () -> Unit): HomeRobot {
    setContent(content)
    return HomeSemantics { HomeRobot() }
}
