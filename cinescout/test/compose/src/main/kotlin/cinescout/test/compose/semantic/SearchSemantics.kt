package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class SearchSemantics {

    fun screen() = onNodeWithTag(TestTag.Search)
    fun searchField() = onNode(isFocusable())
    fun searchQuery(query: String) = onNode(hasText(query) and isFocusable())
    fun title() = onNode(hasText(string.search) and isSelectable().not())
    fun title(title: String) = onNodeWithText(title)
}

context(ComposeUiTest)
fun <T> SearchSemantics(block: context(SearchSemantics) () -> T): T =
    with(SearchSemantics(), block)
