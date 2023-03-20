package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.resources.TextRes
import cinescout.test.compose.semantic.ManageAccountSemantics
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest, ManageAccountSemantics)
class ManageAccountRobot {

    fun selectConnectToTmdb(): ManageAccountRobot {
        connectToTmdbButton().performClick()
        return this
    }

    fun selectConnectToTrakt(): ManageAccountRobot {
        connectToTraktButton().performClick()
        return this
    }

    fun verify(block: Verify.() -> Unit): ManageAccountRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest, ManageAccountSemantics)
    class Verify internal constructor() {

        fun accountUsernameIsDisplayed(username: String) {
            onNodeWithText(username).assertIsDisplayed()
        }

        fun connectButtonsAreDisplayed() {
            connectToTmdbButton().assertIsDisplayed()
            connectToTraktButton().assertIsDisplayed()
        }

        fun errorIsDisplayed(message: TextRes) {
            onNodeWithText(message).assertIsDisplayed()
        }

        fun disconnectButtonIsDisplayed() {
            disconnectButton().assertIsDisplayed()
        }

        fun progressIsDisplayed() {
            progress().assertIsDisplayed()
        }

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun titleIsDisplayed() {
            title().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun ManageAccountRobot(content: @Composable () -> Unit): ManageAccountRobot {
    setContent(content)
    return ManageAccountSemantics { ManageAccountRobot() }
}
