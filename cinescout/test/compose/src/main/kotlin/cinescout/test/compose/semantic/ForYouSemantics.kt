package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ForYouSemantics {

    fun openDetailsButton() = onNodeWithTag(TestTag.ForYouItem)
    fun screen() = onNodeWithTag(TestTag.ForYou)
    fun title() = onNode(hasText(string.suggestions_for_you) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ForYouSemantics(block: context(ForYouSemantics) () -> T): T =
    with(ForYouSemantics(), block)
