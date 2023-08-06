package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.test.compose.semantic.ReportSemantics

context(ComposeUiTest, ReportSemantics)
class ReportRobot {

    fun verify(block: Verify.() -> Unit): ReportRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, ReportSemantics)
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
fun ReportRobot(content: @Composable () -> Unit): ReportRobot {
    setContent(content)
    return ReportSemantics { ReportRobot() }
}
