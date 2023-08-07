package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.test.compose.semantic.ReportBugSemantics

context(ComposeUiTest, ReportBugSemantics)
class ReportBugRobot {

    fun verify(block: Verify.() -> Unit): ReportBugRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, ReportBugSemantics)
    class Verify internal constructor() {

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun titleIsDisplayed() {
            title().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun ReportRobot(content: @Composable () -> Unit): ReportBugRobot {
    setContent(content)
    return ReportBugSemantics { ReportBugRobot() }
}
