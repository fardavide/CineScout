package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class MyListsSemantics {

    fun dislikedButton() = onNode(hasText(string.lists_disliked) and isSelectable().not())
    fun likedButton() = onNode(hasText(string.lists_liked) and isSelectable().not())
    fun ratedButton() = onNode(hasText(string.lists_rated) and isSelectable().not())
    fun screen() = onNodeWithTag(TestTag.MyLists)
    fun subtitle() = onNode(hasText(string.lists_my_lists) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> MyListsSemantics(block: context(MyListsSemantics) () -> T): T =
    with(MyListsSemantics(), block)
