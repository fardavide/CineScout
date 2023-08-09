package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ReportBugSemantics {

    fun screen() = onNodeWithTag(TestTag.BugReport)
    fun title() = onNodeWithText(string.report_report_bug)
}

context(ComposeUiTest)
fun <T> ReportBugSemantics(block: context(ReportBugSemantics) () -> T): T =
    with(ReportBugSemantics(), block)
