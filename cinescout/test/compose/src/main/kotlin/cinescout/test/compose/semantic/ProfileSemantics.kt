package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText

context(ComposeUiTest)
class ProfileSemantics {

    fun manageAccount(): SemanticsNodeInteraction = TODO("not defined yet")
    fun screen() = onNodeWithTag(TestTag.Profile)
    fun title() = onNode(hasText(string.profile) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ProfileSemantics(block: context(ProfileSemantics) () -> T): T =
    with(ProfileSemantics(), block)
