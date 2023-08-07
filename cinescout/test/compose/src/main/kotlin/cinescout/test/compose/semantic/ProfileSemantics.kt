package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.compose.util.hasText
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ProfileSemantics {

    fun appVersion(version: Int) = onNodeWithText(TextRes(string.app_version, version))
    fun manageAccount() = onNodeWithText(string.profile_manage_account_hint)
    fun manageAccountHint() = onNodeWithText(string.profile_manage_account_hint)
    fun requestFeature() = onNodeWithText(string.report_request_feature)
    fun reportBug() = onNodeWithText(string.report_report_bug)
    fun screen() = onNodeWithTag(TestTag.Profile)
    fun settings() = onNodeWithText(string.settings)
    fun title() = onNode(hasText(string.profile) and isSelectable().not())
    fun username(text: String) = onNode(hasText(text) and isSelectable().not())
}

context(ComposeUiTest)
fun <T> ProfileSemantics(block: context(ProfileSemantics) () -> T): T =
    with(ProfileSemantics(), block)
