package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import cinescout.resources.R.string
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithContentDescription

context(ComposeUiTest)
class HomeSemantics {

    fun forYou() = onNode(hasText(string.suggestions_for_you) and isSelectable())
    fun myLists() = onNode(hasText(string.lists_my_lists) and isSelectable())
    fun profile() = onNode(hasText(string.profile) and isSelectable())
    fun profilePicture() = onNodeWithContentDescription(string.profile_picture_description)
}

context(ComposeUiTest)
fun <T> HomeSemantics(block: context(HomeSemantics) () -> T): T =
    with(HomeSemantics(), block)
