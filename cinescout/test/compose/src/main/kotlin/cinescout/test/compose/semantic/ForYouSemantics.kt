package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class ForYouSemantics {

    fun screen() = onNodeWithTag(TestTag.ForYou)
    fun subtitle() = onNode(hasText(string.suggestions_for_you) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ForYouSemantics(block: context(ForYouSemantics) () -> T): T =
    with(ForYouSemantics(), block)
