package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class ItemsListSemantics {

    fun screen() = onNodeWithTag(TestTag.MyLists)
    fun title() = onNode(hasText(string.lists_my_lists) and isSelectable().not())
    fun title(title: String) = onNodeWithTag(title)
}

context(ComposeUiTest)
fun <T> ItemsListSemantics(block: context(ItemsListSemantics) () -> T): T =
    with(ItemsListSemantics(), block)
