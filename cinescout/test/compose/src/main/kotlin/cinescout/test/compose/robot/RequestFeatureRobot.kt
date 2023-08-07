package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.test.compose.semantic.RequestFeatureSemantics

context(ComposeUiTest, RequestFeatureSemantics)
class RequestFeatureRobot {

    fun verify(block: Verify.() -> Unit): RequestFeatureRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, RequestFeatureSemantics)
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
fun RequestFeatureRobotRobot(content: @Composable () -> Unit): RequestFeatureRobot {
    setContent(content)
    return RequestFeatureSemantics { RequestFeatureRobot() }
}
