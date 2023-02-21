package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.test.compose.util.onNodeWithContentDescription
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ManageAccountSemantics {

    fun backButton() = onNodeWithContentDescription(string.back_button_description)
    fun connectToTmdbButton() = onNodeWithText(string.manage_account_connect_to_tmdb)
    fun connectToTraktButton() = onNodeWithText(string.manage_account_connect_to_trakt)
    fun disconnectButton() = onNodeWithText(string.manage_account_disconnect)
    fun progress() = onNodeWithTag(TestTag.Progress)
}

context(ComposeUiTest)
fun <T> ManageAccountSemantics(block: context(ManageAccountSemantics) () -> T): T =
    with(ManageAccountSemantics(), block)