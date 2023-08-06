package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class ReportSemantics {

    fun screen() = onNodeWithTag(TestTag.Report)
    fun title() = onNodeWithText(string.report_report_bug)
}

context(ComposeUiTest)
fun <T> ReportSemantics(block: context(ReportSemantics) () -> T): T =
    with(ReportSemantics(), block)
