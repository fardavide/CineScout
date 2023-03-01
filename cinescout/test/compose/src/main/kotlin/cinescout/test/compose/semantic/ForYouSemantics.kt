package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ForYouSemantics {

    fun openDetailsButton() = onNodeWithText(string.suggestions_for_you_open_details, ignoreCase = true)
    fun screen() = onNodeWithTag(TestTag.ForYou)
    fun title() = onNode(hasText(string.suggestions_for_you) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ForYouSemantics(block: context(ForYouSemantics) () -> T): T =
    with(ForYouSemantics(), block)
