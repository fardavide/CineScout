package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class SettingsSemantics {

    fun screen() = onNodeWithTag(TestTag.Settings)
    fun title() = onNodeWithText(string.settings)
}

context(ComposeUiTest)
fun <T> SettingsSemantics(block: context(SettingsSemantics) () -> T): T =
    with(SettingsSemantics(), block)
