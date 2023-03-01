package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ProfileSemantics {

    fun manageAccount() = onNodeWithText(string.profile_manage_account_hint)
    fun manageAccountHint() = onNodeWithText(string.profile_manage_account_hint)
    fun screen() = onNodeWithTag(TestTag.Profile)
    fun title() = onNode(hasText(string.profile) and isSelectable().not())
    fun username(text: String) = onNode(hasText(text) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ProfileSemantics(block: context(ProfileSemantics) () -> T): T =
    with(ProfileSemantics(), block)
