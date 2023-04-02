package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ManageAccountSemantics {

    fun connectToTraktButton() = onNodeWithText(string.manage_account_connect_to_trakt)
    fun disconnectButton() = onNodeWithText(string.manage_account_disconnect)
    fun progress() = onNodeWithTag(TestTag.Progress)
    fun screen() = onNodeWithTag(TestTag.ManageAccount)
    fun title() = onNodeWithText(string.manage_account)
}

context(ComposeUiTest)
fun <T> ManageAccountSemantics(block: context(ManageAccountSemantics) () -> T): T =
    with(ManageAccountSemantics(), block)
