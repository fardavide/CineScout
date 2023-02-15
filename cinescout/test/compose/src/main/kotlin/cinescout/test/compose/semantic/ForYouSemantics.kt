package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ForYouSemantics {

    fun screen() = onNodeWithTag(TestTag.ForYou)
    fun subtitle() = onNodeWithText(string.suggestions_for_you)
}

context(ComposeUiTest)
fun <T> ForYouSemantics(block: context(ForYouSemantics) () -> T): T =
    with(ForYouSemantics(), block)
