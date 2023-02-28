package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import cinescout.design.R.string
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class HomeSemantics {

    fun forYou() = onNode(hasText(string.suggestions_for_you) and isSelectable())
    fun myLists() = onNode(hasText(string.lists_my_lists) and isSelectable())
    fun profile() = onNode(hasText(string.profile) and isSelectable())
}

context(ComposeUiTest)
fun <T> HomeSemantics(block: context(HomeSemantics) () -> T): T =
    with(HomeSemantics(), block)
