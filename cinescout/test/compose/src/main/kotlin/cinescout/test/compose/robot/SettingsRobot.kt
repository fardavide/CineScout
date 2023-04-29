package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import cinescout.test.compose.semantic.SettingsSemantics

context(ComposeUiTest, SettingsSemantics)
class SettingsRobot {

    fun verify(block: Verify.() -> Unit): SettingsRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, SettingsSemantics)
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
fun SettingsRobot(content: @Composable () -> Unit): SettingsRobot {
    setContent(content)
    return SettingsSemantics { SettingsRobot() }
}
